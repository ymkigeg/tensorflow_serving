# tensorflow_serving
## 安装Tensorflow Serving
参见: [安装](INSTALL.md)

## Estimator训练和保存模型
[实现代码](dnn_model)

## 启动Serving
```bash
# 启动 build_raw_serving_input_receiver_fn 定义输入的模型，直接喂入 TensorProto
docker run -p 8500:8500 -p 8501:8501 \
   --mount type=bind,source=/home/gaoqing/tensorflow_serving/tf_model_raw,target=/models/test_dnn_raw \
   -t --entrypoint=tensorflow_model_server root/tensorflow-serving:latest \
   --port=8500 --rest_api_port=8501 \
   --enable_batching=true --model_name=test_dnn_raw --model_base_path=/models/test_dnn_raw &
   
# 启动 build_parsing_serving_input_receiver_fn 定义输入的模型，需要喂入 ExampleProto
docker run -p 9000:9000 -p 9001:9001 \
   --mount type=bind,source=/home/gaoqing/tensorflow_serving/tf_model,target=/models/test_dnn \
   -t --entrypoint=tensorflow_model_server root/tensorflow-serving:latest \
   --port=9000 --rest_api_port=9001 \
   --enable_batching=true --model_name=test_dnn --model_base_path=/models/test_dnn &
```

## 利用配置文件启动serving
脚本见 [run.sh](run.sh)  
配置文件见 [models.config](models.config)

## gRPC调用
* [python 实现](client/python)
* [Java 实现](client/java)
