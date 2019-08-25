import numpy as np
import requests
import json
import base64

from tensorflow_serving.apis import predict_pb2, prediction_service_pb2 as prediction_service_pb2_grpc

import grpc
import tensorflow as tf

host = '127.0.0.1'


def toy_demo():
    port = 8500
    channel = grpc.insecure_channel('{host}:{port}'.format(host=host, port=port))
    # channel = implementations.insecure_channel(host, int(port))

    stub = prediction_service_pb2_grpc.PredictionServiceStub(channel)
    request = predict_pb2.PredictRequest()
    request.model_spec.name = 'half_plus_two'
    request.model_spec.signature_name = "serving_default"

    request.inputs["x"].CopyFrom(tf.make_tensor_proto([1.0], shape=[1]))

    response = stub.Predict(request, 5.0)
    print(response)


def grpc_predict_raw(input):
    port = 8500
    channel = grpc.insecure_channel('{host}:{port}'.format(host=host, port=port))
    # channel = implementations.insecure_channel(host, int(port))

    stub = prediction_service_pb2_grpc.PredictionServiceStub(channel)
    request = predict_pb2.PredictRequest()
    request.model_spec.name = 'test_dnn_raw'
    request.model_spec.signature_name = "serving_default"

    tensor_protos = {
        'last_note_creators': tf.make_tensor_proto(input['last_note_creators'], dtype=tf.string, shape=[1, 50]),
        'last_note_ids': tf.make_tensor_proto(input['last_note_ids'], dtype=tf.int64, shape=[1, 50]),
        'note_open_id': tf.make_tensor_proto(input['note_open_id'], dtype=tf.string, shape=[1]),
        'note_id': tf.make_tensor_proto(input['note_id'], dtype=tf.int64, shape=[1]),
        'note_video_duration': tf.make_tensor_proto(input['note_video_duration'], dtype=tf.int64, shape=[1])
    }
    for k in tensor_protos:
        request.inputs[k].CopyFrom(tensor_protos[k])

    response = stub.Predict(request, 5.0)
    print(response)


def grpc_predict_example(input_datas):
    port = 9000
    channel = grpc.insecure_channel('{host}:{port}'.format(host=host, port=port))
    # channel = implementations.insecure_channel(host, int(port))

    stub = prediction_service_pb2_grpc.PredictionServiceStub(channel)
    request = predict_pb2.PredictRequest()
    request.model_spec.name = 'test_dnn'
    request.model_spec.signature_name = "serving_default"

    feature = {
        'last_note_creators': tf.train.Feature(bytes_list=tf.train.BytesList(value=input_datas['last_note_creators'])),
        'last_note_ids': tf.train.Feature(int64_list=tf.train.Int64List(value=input_datas['last_note_ids'])),
        'note_open_id': tf.train.Feature(bytes_list=tf.train.BytesList(value=[input_datas['note_open_id']])),
        'note_id': tf.train.Feature(int64_list=tf.train.Int64List(value=[input_datas['note_id']])),
        'note_video_duration': tf.train.Feature(float_list=tf.train.FloatList(value=[input_datas['note_video_duration']])),
    }

    print(feature)

    example_proto = tf.train.Example(features=tf.train.Features(feature=feature))
    serialize_example = example_proto.SerializeToString()
    request.inputs['examples'].CopyFrom(tf.make_tensor_proto(serialize_example,
                                                             dtype=tf.string, shape=[1]))

    response = stub.Predict(request, 5.0)
    print(response.outputs)


with open('../../data/data.json', 'r') as f:
    predict_input = json.loads(f.read())
    print(predict_input)
    predict_input['last_note_creators'] = [s.encode('utf-8') for s in predict_input['last_note_creators']]
    predict_input['note_open_id'] = predict_input['note_open_id'].encode('utf-8')
    grpc_predict_example(predict_input)




