
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.ByteString;
import com.google.protobuf.Int64Value;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import org.tensorflow.example.*;
import org.tensorflow.framework.DataType;
import org.tensorflow.framework.TensorProto;
import org.tensorflow.framework.TensorShapeProto;
import org.tensorflow.framework.TensorShapeProto.Dim;

import tensorflow.serving.Model.ModelSpec;
import tensorflow.serving.Predict.PredictRequest;
import tensorflow.serving.Predict.PredictResponse;
import tensorflow.serving.PredictionServiceGrpc;
import tensorflow.serving.PredictionServiceGrpc.PredictionServiceBlockingStub;
import tensorflow.serving.Predict;
import tensorflow.serving.Model;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class grpc_client {

    public static Map<String, TensorProto> parseTensorProto(JSONObject jsonObject) {
        Map<String, TensorProto> tensorProtoMap = new HashMap<String, TensorProto>();
        for (String key : jsonObject.keySet()) {
            TensorProto.Builder tensorProtoBuilder = TensorProto.newBuilder();

            TensorShapeProto.Builder tensorShapeBuilder = TensorShapeProto.newBuilder();
            tensorShapeBuilder.addDim(TensorShapeProto.Dim.newBuilder().setSize(1));
            if (jsonObject.get(key) instanceof List) {
                tensorShapeBuilder.addDim(TensorShapeProto.Dim.newBuilder().setSize(50));
            } else {
                tensorShapeBuilder.addDim(TensorShapeProto.Dim.newBuilder().setSize(1));
            }
            tensorProtoBuilder.setTensorShape(tensorShapeBuilder.build());

            if (key.equals("note_open_id")) {
                tensorProtoBuilder.setDtype(DataType.DT_STRING);
                ByteString bytes = ByteString.copyFromUtf8((String)jsonObject.get(key));
                tensorProtoBuilder.addStringVal(bytes);
            } else if (key.equals("note_video_duration") || key.equals("note_id")) {
                tensorProtoBuilder.setDtype(DataType.DT_INT64);
                tensorProtoBuilder.addInt64Val((Integer)jsonObject.get(key));
            } else if (key.equals("last_note_ids")) {
                tensorProtoBuilder.setDtype(DataType.DT_INT64);
                List<Integer> integers = (List<Integer>)jsonObject.get(key);
                List<Long> longs = new ArrayList<Long>();
                for (Integer i : integers) {
                    longs.add(i.longValue());
                }
                tensorProtoBuilder.addAllInt64Val(longs);
            } else if (key.equals("last_note_creators")) {
                tensorProtoBuilder.setDtype(DataType.DT_STRING);
                List<String> stringList = (List<String>)jsonObject.get(key);
                List<ByteString> byteStrings = new ArrayList<ByteString>();
                for (String s : stringList) {
                    byteStrings.add(ByteString.copyFromUtf8(s));
                }
                tensorProtoBuilder.addAllStringVal(byteStrings);
            }

            tensorProtoMap.put(key, tensorProtoBuilder.build());
        }
        return tensorProtoMap;
    }

    public static Map<String, TensorProto> parseExampleProto(JSONObject jsonObject) {
        Map<String, Feature> inputFeatureMap = new HashMap<String, Feature>();

        for (String key : jsonObject.keySet()) {
            Feature feature = null;

            if (key.equals("note_open_id")) {
                BytesList.Builder byteListBuilder = BytesList.newBuilder();
                ByteString bytes = ByteString.copyFromUtf8((String)jsonObject.get(key));
                byteListBuilder.addValue(bytes);
                feature = Feature.newBuilder().setBytesList(byteListBuilder.build()).build();
            } else if (key.equals("note_id")) {
                Int64List.Builder int64ListBuilder = Int64List.newBuilder();
                int64ListBuilder.addValue((Integer)jsonObject.get(key));
                feature = Feature.newBuilder().setInt64List(int64ListBuilder.build()).build();
            } else if (key.equals("note_video_duration")) {
                FloatList.Builder floatListBuilder = FloatList.newBuilder();
                floatListBuilder.addValue((Integer)jsonObject.get(key));
                feature = Feature.newBuilder().setFloatList(floatListBuilder.build()).build();
            } else if (key.equals("last_note_ids")) {
                List<Integer> integers = (List<Integer>)jsonObject.get(key);
                List<Long> longs = new ArrayList<Long>();
                for (Integer i : integers) {
                    longs.add(i.longValue());
                }
                Int64List.Builder int64ListBuilder = Int64List.newBuilder();
                int64ListBuilder.addAllValue(longs);
                feature = Feature.newBuilder().setInt64List(int64ListBuilder.build()).build();
            } else if (key.equals("last_note_creators")) {
                List<String> stringList = (List<String>)jsonObject.get(key);
                List<ByteString> byteStrings = new ArrayList<ByteString>();
                for (String s : stringList) {
                    byteStrings.add(ByteString.copyFromUtf8(s));
                }
                BytesList.Builder byteListBuilder = BytesList.newBuilder();
                byteListBuilder.addAllValue(byteStrings);
                feature = Feature.newBuilder().setBytesList(byteListBuilder.build()).build();
            }

            if (feature != null) {
                inputFeatureMap.put(key, feature);
            }
        }

        Features features = Features.newBuilder().putAllFeature(inputFeatureMap).build();
        ByteString inputStr = Example.newBuilder().setFeatures(features).build().toByteString();

        TensorShapeProto.Builder tensorShapeBuilder = TensorShapeProto.newBuilder();
        tensorShapeBuilder.addDim(TensorShapeProto.Dim.newBuilder().setSize(1).build());

        TensorProto proto = TensorProto.newBuilder()
                .addStringVal(inputStr)
                .setTensorShape(tensorShapeBuilder.build())
                .setDtype(DataType.DT_STRING)
                .build();

        Map<String, TensorProto> tensorProtoMap = new HashMap<String, TensorProto>();
        tensorProtoMap.put("examples", proto);
        return tensorProtoMap;
    }

    public static PredictResponse predictWithRawInput(JSONObject jsonObject, String host, int port, Integer modelVersion) {
        final String MODEL_NAME = "test_dnn_raw";
        final String SIGNATURE_NAME = "serving_default";

        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();

        //这里还是先用block模式
        PredictionServiceBlockingStub stub = PredictionServiceGrpc.newBlockingStub(channel);
        //创建请求
        Predict.PredictRequest.Builder predictRequestBuilder = Predict.PredictRequest.newBuilder();

        //模型名称和模型方法名预设
        Model.ModelSpec.Builder modelSpecBuilder = Model.ModelSpec.newBuilder();
        modelSpecBuilder.setName(MODEL_NAME);
        modelSpecBuilder.setSignatureName(SIGNATURE_NAME);
        modelSpecBuilder.setVersion(Int64Value.newBuilder().setValue(modelVersion));
        predictRequestBuilder.setModelSpec(modelSpecBuilder);

        //设置入参,访问默认是最新版本，如果需要特定版本可以使用tensorProtoBuilder.setVersionNumber方法
        Map<String, TensorProto> tensorProtoMap = parseTensorProto(jsonObject);

        predictRequestBuilder.putAllInputs(tensorProtoMap);

        //访问并获取结果
        PredictResponse predictResponse = stub.predict(predictRequestBuilder.build());
        return predictResponse;
    }

    public static PredictResponse predictWithExampleInput(JSONObject jsonObject, String host, int port, Integer modelVersion) {
        final String MODEL_NAME = "test_dnn";
        final String SIGNATURE_NAME = "serving_default";

        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();

        //这里还是先用block模式
        PredictionServiceBlockingStub stub = PredictionServiceGrpc.newBlockingStub(channel);
        //创建请求
        Predict.PredictRequest.Builder predictRequestBuilder = Predict.PredictRequest.newBuilder();

        //模型名称和模型方法名预设
        Model.ModelSpec.Builder modelSpecBuilder = Model.ModelSpec.newBuilder();
        modelSpecBuilder.setName(MODEL_NAME);
        modelSpecBuilder.setSignatureName(SIGNATURE_NAME);
        modelSpecBuilder.setVersion(Int64Value.newBuilder().setValue(modelVersion));
        predictRequestBuilder.setModelSpec(modelSpecBuilder);

        //设置入参,访问默认是最新版本，如果需要特定版本可以使用tensorProtoBuilder.setVersionNumber方法
        Map<String, TensorProto> tensorProtoMap = parseExampleProto(jsonObject);

        predictRequestBuilder.putAllInputs(tensorProtoMap);

        //访问并获取结果
        PredictResponse predictResponse = stub.predict(predictRequestBuilder.build());
        return predictResponse;
    }

    public static void main(String[] args) throws IOException{
        Integer modelVersion = 1;
        String host = "127.0.0.1";

        String dataPath = "../../../data/data.json";
        InputStreamReader input = new InputStreamReader(new FileInputStream(new File(dataPath)));
        BufferedReader bf = new BufferedReader(input);
        String json = bf.readLine();
        JSONObject jsonObject = JSON.parseObject(json);

//        PredictResponse response = predictWithRawInput(jsonObject, host, 8500, modelVersion);
        PredictResponse response = predictWithExampleInput(jsonObject, host, 9000, modelVersion);

        System.out.println(response.getOutputsMap());

    }
}
