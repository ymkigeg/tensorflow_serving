# -*- coding:utf-8 -*-

import sys
import os
import argparse
import traceback
import json

import tensorflow as tf
import numpy as np
from tensorflow import feature_column as fc
from tensorflow.python.estimator.canned import head as head_lib
from tensorflow.python.framework import ops
from tensorflow.python.saved_model import signature_constants

from exporter import BestExporter, auc_bigger


FLAGS = None

_CSV_COLUMNS = ['dt', 'open_id', 'note_id', 'note_open_id', 'watch_time', 'note_video_duration', 'client_time',
                'server_time', 'is_like', 'last_note_creators', 'last_note_ids']
_CSV_COLUMN_DEFAULTS = [['19700101'], [''], [0], [''], [0], [0], ['1970-01-01 08:00:00'],
                        ['1970-01-01 08:00:00'], [0], [''], ['']]


def set_tfconfig_environ(FLAGS, choose_ps_as_evaluate=False):
    parse_argument(FLAGS)

    if "TF_CLUSTER_DEF" in os.environ:
        cluster = json.loads(os.environ["TF_CLUSTER_DEF"])
        task_index = int(os.environ["TF_INDEX"])
        task_type = os.environ["TF_ROLE"]

        tf_config = dict()
        worker_num = len(cluster["worker"])
        FLAGS.worker_num = worker_num
        if task_type == "ps":
            # 把第一个ps转为evaluator
            if len(cluster["ps"]) >= 2 and choose_ps_as_evaluate:
                if task_index == 0:
                    tf_config["task"] = {"index": 0, "type": "evaluator"}
                    FLAGS.job_name = "evaluator"
                    FLAGS.task_index = 0
                else:
                    tf_config["task"] = {"index": task_index - 1, "type": task_type}
                    FLAGS.job_name = "ps"
                    FLAGS.task_index = task_index - 1
            else:
                tf_config["task"] = {"index": task_index, "type": task_type}
                FLAGS.job_name = "ps"
                FLAGS.task_index = task_index
        else:
            if task_index == 0:
                tf_config["task"] = {"index": 0, "type": "chief"}
            else:
                tf_config["task"] = {"index": task_index - 1, "type": task_type}
            FLAGS.job_name = "worker"
            FLAGS.task_index = task_index

        if worker_num == 1:
            cluster["chief"] = cluster["worker"]
            del cluster["worker"]
        else:
            cluster["chief"] = [cluster["worker"][0]]
            del cluster["worker"][0]

        if len(cluster["ps"]) >= 2 and choose_ps_as_evaluate:
            del cluster["ps"][0]

        tf_config["cluster"] = cluster
        os.environ["TF_CONFIG"] = json.dumps(tf_config)
        print("TF_CONFIG", json.loads(os.environ["TF_CONFIG"]))

    # if "INPUT_FILE_LIST" in os.environ:
    #     INPUT_PATH = json.loads(os.environ["INPUT_FILE_LIST"])
    #     if INPUT_PATH:
    #         print("input path:", INPUT_PATH)
    #         FLAGS.train_data = INPUT_PATH.get(FLAGS.train_data)
    #         FLAGS.eval_data = INPUT_PATH.get(FLAGS.eval_data)
    #     else:  # for ps
    #         print("load input path failed.")
    #         FLAGS.train_data = None
    #         FLAGS.eval_data = None


def parse_argument(FLAGS):
    if FLAGS.job_name is None or FLAGS.job_name == "":
        raise ValueError("Must specify an explicit `job_name`")

    if FLAGS.task_index is None or FLAGS.task_index == "":
        raise ValueError("Must specify an explicit `task_index`")

    print("job name = %s" % FLAGS.job_name)
    print("task index = %d" % FLAGS.task_index)

    os.environ["TF_ROLE"] = FLAGS.job_name
    os.environ["TF_INDEX"] = str(FLAGS.task_index)

    # Construct the cluster and start the server
    ps_spec = FLAGS.ps_hosts.split(",")
    worker_spec = FLAGS.worker_hosts.split(",")

    cluster = {"worker": worker_spec, "ps": ps_spec}

    os.environ["TF_CLUSTER_DEF"] = json.dumps(cluster)


def create_feature_columns(note_emb_size=10, note_user_emb_size=6):
    # 先创建分类列

    creator_ids = fc.categorical_column_with_hash_bucket("last_note_creators", hash_bucket_size=2000, dtype=tf.string)
    note_ids = fc.categorical_column_with_hash_bucket("last_note_ids", 20000, dtype=tf.int64)

    creator_id = fc.categorical_column_with_hash_bucket("note_open_id", 2000)
    note_id = fc.categorical_column_with_hash_bucket("note_id", 20000, dtype=tf.int64)

    video_duration = fc.numeric_column("note_video_duration")
    video_duration_bucket = fc.bucketized_column(
        source_column=video_duration,
        boundaries=[5, 10, 30, 60])

    note_emb = fc.shared_embedding_columns([note_ids, note_id], note_emb_size, combiner='sum')
    creator_emb = fc.shared_embedding_columns([creator_ids, creator_id], note_user_emb_size, combiner='sum')

    my_feature_columns = note_emb + creator_emb + [video_duration_bucket]
    print("*" * 100)
    print("feature columns:")
    for i in my_feature_columns:
        print(i)
    print("*" * 100)
    return my_feature_columns


def build_model_net(features, mode, params):
    net = fc.input_layer(features, params['feature_columns'])
    # net = tf.layers.batch_normalization(net, training=(mode == tf.estimator.ModeKeys.TRAIN))
    # Build the hidden layers, sized according to the 'hidden_units' param.
    for units in params['hidden_units']:
        net = tf.layers.dense(net, units=units, activation=tf.nn.relu)
        if 'dropout_rate' in params and params['dropout_rate'] > 0.0:
            net = tf.layers.dropout(net, params['dropout_rate'], training=(mode == tf.estimator.ModeKeys.TRAIN))
            print ("net node count", net.shape[-1].value)
    logits = tf.layers.dense(net, units=1)
    return logits


def dnn_model_fn(features, labels, mode, params):
    logits = build_model_net(features, mode, params)

    if params['optimizer_type'] == 'adam':
        optimizer = tf.train.AdamOptimizer(learning_rate=params['learning_rate'])
    elif params['optimizer_type'] == 'adagrad':
        optimizer = tf.train.AdagradOptimizer(learning_rate=params['learning_rate'])
    else:
        optimizer = tf.train.GradientDescentOptimizer(learning_rate=params['learning_rate'])

    y = tf.sigmoid(logits)

    if mode == tf.estimator.ModeKeys.PREDICT:
        default_output_key = signature_constants.DEFAULT_SERVING_SIGNATURE_DEF_KEY
        export_outputs = {
            # default_output_key: tf.estimator.export.PredictOutput(y),
            'predict': tf.estimator.export.PredictOutput(y),
            # 'classify': tf.estimator.export.ClassificationOutput(y),
        }
        # export_outputs 决定了导出模型输出有哪些种类
        # 这里只定义了 predict, 因此输出就只有 predict 和 serving_default
        spec = tf.estimator.EstimatorSpec(mode=mode,
                                          predictions=y,
                                          export_outputs=export_outputs)

    else:
        cross_entropy = tf.losses.sigmoid_cross_entropy(labels, logits=logits)
        loss = tf.reduce_mean(cross_entropy, name="loss")

        # Get the TensorFlow op for doing a single optimization step.
        train_op = optimizer.minimize(
            loss=loss, global_step=tf.train.get_global_step())

        # Define the evaluation metrics,
        # in this case the classification accuracy.
        metrics = {
            'auc': tf.metrics.auc(labels, y, name='auc'),
        }

        # Wrap all of this in an EstimatorSpec.
        spec = tf.estimator.EstimatorSpec(
            mode=mode,
            loss=loss,
            train_op=train_op,
            eval_metric_ops=metrics)

    return spec


def list_resize(value, size, constant_values=-1):
    if len(value) > size:
        return value[0:size]
    else:
        result = [constant_values] * size
        for i in range(len(value)):
            result[i] = value[i]
        return result


def parse_line(line):
    _STR_COLUMNS = ['dt', 'open_id', 'note_open_id', 'client_time', 'server_time']
    _INT_COLUMNS = ['watch_time', 'note_id', 'note_video_duration', 'is_like']
    _SEQ_COLUMNS = ['last_note_creators', 'last_note_ids']

    def get_content(record):
        columns = record.split(',')
        features = dict(zip(_CSV_COLUMNS, columns))

        last_note_creators = features['last_note_creators'].split('#')
        features['last_note_creators'] = list_resize(last_note_creators, 50, '-1')

        last_note_ids = features['last_note_ids'].split('#')
        last_note_ids = [np.int64(nid) for nid in last_note_ids]
        features['last_note_ids'] = list_resize(last_note_ids, 50, -1)

        elems = []
        for col in _STR_COLUMNS:
            elems += [features[col]]
        for col in _INT_COLUMNS:
            elems += [np.int64(features[col])]
        for col in _SEQ_COLUMNS:
            elems += [features[col]]
        return elems

    out_type = [tf.string] * 5 + [tf.int64] * 4 + [tf.string, tf.int64]

    result = tf.py_func(get_content, [line], out_type)
    n = len(result) - 2
    for i in range(n):
        result[i].set_shape([])
    for i in range(n, len(result)):
        result[i].set_shape([50])

    columns = _STR_COLUMNS + _INT_COLUMNS + _SEQ_COLUMNS
    features = dict(zip(columns, result))

    labels = features.pop('is_like')
    labels = tf.reshape(labels, [1])

    return features, labels


def create_dataset_from_file(filenames, batch_size, num_epochs=None, shuffle_buffer_size=0):
    dataset_files = tf.data.Dataset.from_tensor_slices(filenames)

    dataset = dataset_files.interleave(
        lambda x:
            tf.data.TextLineDataset(x).skip(1).map(parse_line,
                                                   num_parallel_calls=8),  # 并行的进行map
        cycle_length=4,  # 控制并发读取数量
        block_length=1)
    # dataset_files = tf.data.Dataset.list_files(self.filenames)
    # dataset = dataset_files.apply(parallel_interleave(
    #     lambda x:
    #         tf.data.TextLineDataset(x).map(self.decode_csv_fn,
    #                                        num_parallel_calls=8),  # 并行的进行map
    #     cycle_length=4,  # 控制并发读取数量
    #     block_length=1))
    if shuffle_buffer_size > 0:
        dataset = dataset.shuffle(shuffle_buffer_size)
    dataset = dataset.repeat(num_epochs)  # 重复次数
    dataset = dataset.batch(batch_size)
    dataset = dataset.prefetch(1)  # 实现流水线
    return dataset


def main(_):
    if len(FLAGS.ps_hosts) > 1 and len(FLAGS.worker_hosts) > 1:
        set_tfconfig_environ(FLAGS, FLAGS.choose_ps_as_evaluate)

    print(tf.VERSION)

    FLAGS.batch_size = FLAGS.task_batch_size * FLAGS.worker_num

    total = 10402791  # 16333644
    epoch_steps = total // FLAGS.batch_size
    save_checkpoints_steps = epoch_steps // 10
    max_steps = FLAGS.num_epochs * epoch_steps

    print('total: %d, batch_size: %d, epoch_steps: %d, save_checkpoints_steps: %d, max_steps: %d'
          % (total, FLAGS.batch_size, epoch_steps, save_checkpoints_steps, max_steps))

    my_feature_columns = create_feature_columns()

    estimator = tf.estimator.Estimator(
        model_fn=dnn_model_fn,
        params={
            'feature_columns': my_feature_columns,
            'hidden_units': FLAGS.hidden_units.split(','),
            'optimizer_type': FLAGS.optimizer_type,
            'learning_rate': FLAGS.learning_rate,
            'dropout_rate': FLAGS.dropout_rate
        },
        config=tf.estimator.RunConfig(model_dir=FLAGS.checkpointDir,
                                      save_checkpoints_steps=FLAGS.save_checkpoints_steps)
    )

    train_files = ['../data/train_data.csv']
    if True:
        train_files.sort()
        part_files = []
        for i in range(len(train_files)):
            if FLAGS.task_index == i % FLAGS.worker_num:
                part_files.append(train_files[i])

    train_input_fn = lambda: create_dataset_from_file(part_files, FLAGS.batch_size,
                                                      num_epochs=FLAGS.num_epochs,
                                                      shuffle_buffer_size=FLAGS.shuffle_buffer_size)

    test_files = ['../data/test_data.csv']
    test_input_fn = lambda: create_dataset_from_file(test_files, FLAGS.batch_size)

    train_spec = tf.estimator.TrainSpec(input_fn=train_input_fn, max_steps=max_steps)

    # eval_hooks = [
    #     EarlyStoppingHook()
    # ]

    serving_feature_spec = tf.feature_column.make_parse_example_spec(my_feature_columns)
    feature_placeholder = {
        'last_note_creators': tf.placeholder(tf.string, [50], name='last_note_creators'),
        'last_note_ids': tf.placeholder(tf.int64, [50], name='last_note_ids'),
        'note_open_id': tf.placeholder(tf.string, [1], name='note_open_id'),
        'note_id': tf.placeholder(tf.int64, [1], name='note_id'),
        'note_video_duration': tf.placeholder(tf.int64, [1], name='note_video_duration')
    }

    # 这里可以有两种方式来创建 serving_input_receiver_fn
    # 函数不同，决定了serving时，输入的方式也不同
    # build_parsing_serving_input_receiver_fn 要求输入序列化后的tf.train.Example
    # build_raw_serving_input_receiver_fn 则直接输入 TensorProto 字典
    serving_input_receiver_fn = (
        # tf.estimator.export.build_parsing_serving_input_receiver_fn(serving_feature_spec)
        tf.estimator.export.build_raw_serving_input_receiver_fn(feature_placeholder)
    )

    exporters = [
        BestExporter(
            name="best_exporter",
            serving_input_receiver_fn=serving_input_receiver_fn,
            compare_fn=auc_bigger,
            exports_to_keep=5)
    ]

    eval_spec = tf.estimator.EvalSpec(input_fn=test_input_fn, steps=200, throttle_secs=60, exporters=exporters)
    # hooks=eval_hooks)

    try:
        tf.estimator.train_and_evaluate(estimator, train_spec, eval_spec)
    except ValueError as ve:
        print("training stopped")
        print("ValueError exception", ve)
        traceback.print_exc()

    # for epoch in range(FLAGS.num_epochs):
    #     estimator.train(train_input_fn, max_steps=max_steps)
    #     results = estimator.evaluate(test_input_fn)
    #     for key in sorted(results):
    #         print('%s: %s' % (key, results[key]))

    # Evaluate accuracy.
    results = estimator.evaluate(input_fn=test_input_fn)
    for key in sorted(results):
        print('%s: %s' % (key, results[key]))

    print("after evaluate")


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('--num_epochs', type=int, default=10,
                        help='Number of steps to run trainer.')
    parser.add_argument('--learning_rate', type=float, default=0.01,
                        help='Initial learning rate')
    parser.add_argument('--dropout_rate', type=float, default=0.7,
                        help='Keep probability for training dropout.')
    parser.add_argument('--task_batch_size', type=int, default=64,
                        help='batch size for each task')
    parser.add_argument('--hidden_units', type=str, default='128,64,64',
                        help='hidden_units')
    parser.add_argument('--optimizer_type', type=str, default='adam',
                        help='optimizer type: adam, adagrad, sgd')
    parser.add_argument('--buckets', type=str,
                        default='./data/',
                        help='data directory')

    parser.add_argument('--checkpointDir', type=str,
                        default='./checkpoint/',
                        help='checkpoint output directory')

    # 以下参数仅在多机多卡时有用
    parser.add_argument("--ps_hosts", type=str, default="", help="")
    parser.add_argument("--worker_hosts", type=str, default="", help="")
    parser.add_argument("--job_name", type=str, default="", help="One of 'ps', 'worker'")
    # Flags for defining the tf.train.Server
    parser.add_argument("--task_index", type=int, default=0, help="Index of task within the job")
    parser.add_argument('--worker_num', type=int, default=1,
                        help='Number of train worker.')
    parser.add_argument('--save_checkpoints_steps', type=int, default=100,
                        help='Save checkpoints every this many steps')
    parser.add_argument('--shuffle_buffer_size', type=int, default=10000,
                        help='Save checkpoints every this many steps')
    parser.add_argument('--choose_ps_as_evaluate', type=bool, default=False,
                        help='whether to choose one of ps server as evaluate')

    FLAGS, unparsed = parser.parse_known_args()
    tf.logging.set_verbosity(tf.logging.INFO)
    tf.app.run(main=main, argv=[sys.argv[0]] + unparsed)

