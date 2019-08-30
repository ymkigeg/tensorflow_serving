package tensorflow.serving;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 * <pre>
 * open source marker; do not remove
 * PredictionService provides access to machine-learned models loaded by
 * model_servers.
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.23.0)",
    comments = "Source: prediction_service.proto")
public final class PredictionServiceGrpc {

  private PredictionServiceGrpc() {}

  public static final String SERVICE_NAME = "tensorflow.serving.PredictionService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<Classification.ClassificationRequest,
      Classification.ClassificationResponse> getClassifyMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Classify",
      requestType = Classification.ClassificationRequest.class,
      responseType = Classification.ClassificationResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<Classification.ClassificationRequest,
      Classification.ClassificationResponse> getClassifyMethod() {
    io.grpc.MethodDescriptor<Classification.ClassificationRequest, Classification.ClassificationResponse> getClassifyMethod;
    if ((getClassifyMethod = PredictionServiceGrpc.getClassifyMethod) == null) {
      synchronized (PredictionServiceGrpc.class) {
        if ((getClassifyMethod = PredictionServiceGrpc.getClassifyMethod) == null) {
          PredictionServiceGrpc.getClassifyMethod = getClassifyMethod =
              io.grpc.MethodDescriptor.<Classification.ClassificationRequest, Classification.ClassificationResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Classify"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  Classification.ClassificationRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  Classification.ClassificationResponse.getDefaultInstance()))
              .setSchemaDescriptor(new PredictionServiceMethodDescriptorSupplier("Classify"))
              .build();
        }
      }
    }
    return getClassifyMethod;
  }

  private static volatile io.grpc.MethodDescriptor<RegressionOuterClass.RegressionRequest,
      RegressionOuterClass.RegressionResponse> getRegressMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Regress",
      requestType = RegressionOuterClass.RegressionRequest.class,
      responseType = RegressionOuterClass.RegressionResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<RegressionOuterClass.RegressionRequest,
      RegressionOuterClass.RegressionResponse> getRegressMethod() {
    io.grpc.MethodDescriptor<RegressionOuterClass.RegressionRequest, RegressionOuterClass.RegressionResponse> getRegressMethod;
    if ((getRegressMethod = PredictionServiceGrpc.getRegressMethod) == null) {
      synchronized (PredictionServiceGrpc.class) {
        if ((getRegressMethod = PredictionServiceGrpc.getRegressMethod) == null) {
          PredictionServiceGrpc.getRegressMethod = getRegressMethod =
              io.grpc.MethodDescriptor.<RegressionOuterClass.RegressionRequest, RegressionOuterClass.RegressionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Regress"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  RegressionOuterClass.RegressionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  RegressionOuterClass.RegressionResponse.getDefaultInstance()))
              .setSchemaDescriptor(new PredictionServiceMethodDescriptorSupplier("Regress"))
              .build();
        }
      }
    }
    return getRegressMethod;
  }

  private static volatile io.grpc.MethodDescriptor<Predict.PredictRequest,
      Predict.PredictResponse> getPredictMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Predict",
      requestType = Predict.PredictRequest.class,
      responseType = Predict.PredictResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<Predict.PredictRequest,
      Predict.PredictResponse> getPredictMethod() {
    io.grpc.MethodDescriptor<Predict.PredictRequest, Predict.PredictResponse> getPredictMethod;
    if ((getPredictMethod = PredictionServiceGrpc.getPredictMethod) == null) {
      synchronized (PredictionServiceGrpc.class) {
        if ((getPredictMethod = PredictionServiceGrpc.getPredictMethod) == null) {
          PredictionServiceGrpc.getPredictMethod = getPredictMethod =
              io.grpc.MethodDescriptor.<Predict.PredictRequest, Predict.PredictResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Predict"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  Predict.PredictRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  Predict.PredictResponse.getDefaultInstance()))
              .setSchemaDescriptor(new PredictionServiceMethodDescriptorSupplier("Predict"))
              .build();
        }
      }
    }
    return getPredictMethod;
  }

  private static volatile io.grpc.MethodDescriptor<Inference.MultiInferenceRequest,
      Inference.MultiInferenceResponse> getMultiInferenceMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "MultiInference",
      requestType = Inference.MultiInferenceRequest.class,
      responseType = Inference.MultiInferenceResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<Inference.MultiInferenceRequest,
      Inference.MultiInferenceResponse> getMultiInferenceMethod() {
    io.grpc.MethodDescriptor<Inference.MultiInferenceRequest, Inference.MultiInferenceResponse> getMultiInferenceMethod;
    if ((getMultiInferenceMethod = PredictionServiceGrpc.getMultiInferenceMethod) == null) {
      synchronized (PredictionServiceGrpc.class) {
        if ((getMultiInferenceMethod = PredictionServiceGrpc.getMultiInferenceMethod) == null) {
          PredictionServiceGrpc.getMultiInferenceMethod = getMultiInferenceMethod =
              io.grpc.MethodDescriptor.<Inference.MultiInferenceRequest, Inference.MultiInferenceResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "MultiInference"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  Inference.MultiInferenceRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  Inference.MultiInferenceResponse.getDefaultInstance()))
              .setSchemaDescriptor(new PredictionServiceMethodDescriptorSupplier("MultiInference"))
              .build();
        }
      }
    }
    return getMultiInferenceMethod;
  }

  private static volatile io.grpc.MethodDescriptor<GetModelMetadata.GetModelMetadataRequest,
      GetModelMetadata.GetModelMetadataResponse> getGetModelMetadataMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetModelMetadata",
      requestType = GetModelMetadata.GetModelMetadataRequest.class,
      responseType = GetModelMetadata.GetModelMetadataResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<GetModelMetadata.GetModelMetadataRequest,
      GetModelMetadata.GetModelMetadataResponse> getGetModelMetadataMethod() {
    io.grpc.MethodDescriptor<GetModelMetadata.GetModelMetadataRequest, GetModelMetadata.GetModelMetadataResponse> getGetModelMetadataMethod;
    if ((getGetModelMetadataMethod = PredictionServiceGrpc.getGetModelMetadataMethod) == null) {
      synchronized (PredictionServiceGrpc.class) {
        if ((getGetModelMetadataMethod = PredictionServiceGrpc.getGetModelMetadataMethod) == null) {
          PredictionServiceGrpc.getGetModelMetadataMethod = getGetModelMetadataMethod =
              io.grpc.MethodDescriptor.<GetModelMetadata.GetModelMetadataRequest, GetModelMetadata.GetModelMetadataResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetModelMetadata"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  GetModelMetadata.GetModelMetadataRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  GetModelMetadata.GetModelMetadataResponse.getDefaultInstance()))
              .setSchemaDescriptor(new PredictionServiceMethodDescriptorSupplier("GetModelMetadata"))
              .build();
        }
      }
    }
    return getGetModelMetadataMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static PredictionServiceStub newStub(io.grpc.Channel channel) {
    return new PredictionServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static PredictionServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new PredictionServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static PredictionServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new PredictionServiceFutureStub(channel);
  }

  /**
   * <pre>
   * open source marker; do not remove
   * PredictionService provides access to machine-learned models loaded by
   * model_servers.
   * </pre>
   */
  public static abstract class PredictionServiceImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * Classify.
     * </pre>
     */
    public void classify(Classification.ClassificationRequest request,
        io.grpc.stub.StreamObserver<Classification.ClassificationResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getClassifyMethod(), responseObserver);
    }

    /**
     * <pre>
     * Regress.
     * </pre>
     */
    public void regress(RegressionOuterClass.RegressionRequest request,
        io.grpc.stub.StreamObserver<RegressionOuterClass.RegressionResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getRegressMethod(), responseObserver);
    }

    /**
     * <pre>
     * Predict -- provides access to loaded TensorFlow model.
     * </pre>
     */
    public void predict(Predict.PredictRequest request,
        io.grpc.stub.StreamObserver<Predict.PredictResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getPredictMethod(), responseObserver);
    }

    /**
     * <pre>
     * MultiInference API for multi-headed models.
     * </pre>
     */
    public void multiInference(Inference.MultiInferenceRequest request,
        io.grpc.stub.StreamObserver<Inference.MultiInferenceResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getMultiInferenceMethod(), responseObserver);
    }

    /**
     * <pre>
     * GetModelMetadata - provides access to metadata for loaded models.
     * </pre>
     */
    public void getModelMetadata(GetModelMetadata.GetModelMetadataRequest request,
        io.grpc.stub.StreamObserver<GetModelMetadata.GetModelMetadataResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetModelMetadataMethod(), responseObserver);
    }

    @Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getClassifyMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                Classification.ClassificationRequest,
                Classification.ClassificationResponse>(
                  this, METHODID_CLASSIFY)))
          .addMethod(
            getRegressMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                RegressionOuterClass.RegressionRequest,
                RegressionOuterClass.RegressionResponse>(
                  this, METHODID_REGRESS)))
          .addMethod(
            getPredictMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                Predict.PredictRequest,
                Predict.PredictResponse>(
                  this, METHODID_PREDICT)))
          .addMethod(
            getMultiInferenceMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                Inference.MultiInferenceRequest,
                Inference.MultiInferenceResponse>(
                  this, METHODID_MULTI_INFERENCE)))
          .addMethod(
            getGetModelMetadataMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                GetModelMetadata.GetModelMetadataRequest,
                GetModelMetadata.GetModelMetadataResponse>(
                  this, METHODID_GET_MODEL_METADATA)))
          .build();
    }
  }

  /**
   * <pre>
   * open source marker; do not remove
   * PredictionService provides access to machine-learned models loaded by
   * model_servers.
   * </pre>
   */
  public static final class PredictionServiceStub extends io.grpc.stub.AbstractStub<PredictionServiceStub> {
    private PredictionServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private PredictionServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected PredictionServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new PredictionServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Classify.
     * </pre>
     */
    public void classify(Classification.ClassificationRequest request,
        io.grpc.stub.StreamObserver<Classification.ClassificationResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getClassifyMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Regress.
     * </pre>
     */
    public void regress(RegressionOuterClass.RegressionRequest request,
        io.grpc.stub.StreamObserver<RegressionOuterClass.RegressionResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRegressMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Predict -- provides access to loaded TensorFlow model.
     * </pre>
     */
    public void predict(Predict.PredictRequest request,
        io.grpc.stub.StreamObserver<Predict.PredictResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getPredictMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * MultiInference API for multi-headed models.
     * </pre>
     */
    public void multiInference(Inference.MultiInferenceRequest request,
        io.grpc.stub.StreamObserver<Inference.MultiInferenceResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getMultiInferenceMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * GetModelMetadata - provides access to metadata for loaded models.
     * </pre>
     */
    public void getModelMetadata(GetModelMetadata.GetModelMetadataRequest request,
        io.grpc.stub.StreamObserver<GetModelMetadata.GetModelMetadataResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetModelMetadataMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   * open source marker; do not remove
   * PredictionService provides access to machine-learned models loaded by
   * model_servers.
   * </pre>
   */
  public static final class PredictionServiceBlockingStub extends io.grpc.stub.AbstractStub<PredictionServiceBlockingStub> {
    private PredictionServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private PredictionServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected PredictionServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new PredictionServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Classify.
     * </pre>
     */
    public Classification.ClassificationResponse classify(Classification.ClassificationRequest request) {
      return blockingUnaryCall(
          getChannel(), getClassifyMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Regress.
     * </pre>
     */
    public RegressionOuterClass.RegressionResponse regress(RegressionOuterClass.RegressionRequest request) {
      return blockingUnaryCall(
          getChannel(), getRegressMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Predict -- provides access to loaded TensorFlow model.
     * </pre>
     */
    public Predict.PredictResponse predict(Predict.PredictRequest request) {
      return blockingUnaryCall(
          getChannel(), getPredictMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * MultiInference API for multi-headed models.
     * </pre>
     */
    public Inference.MultiInferenceResponse multiInference(Inference.MultiInferenceRequest request) {
      return blockingUnaryCall(
          getChannel(), getMultiInferenceMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * GetModelMetadata - provides access to metadata for loaded models.
     * </pre>
     */
    public GetModelMetadata.GetModelMetadataResponse getModelMetadata(GetModelMetadata.GetModelMetadataRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetModelMetadataMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * open source marker; do not remove
   * PredictionService provides access to machine-learned models loaded by
   * model_servers.
   * </pre>
   */
  public static final class PredictionServiceFutureStub extends io.grpc.stub.AbstractStub<PredictionServiceFutureStub> {
    private PredictionServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private PredictionServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected PredictionServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new PredictionServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Classify.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<Classification.ClassificationResponse> classify(
        Classification.ClassificationRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getClassifyMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Regress.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<RegressionOuterClass.RegressionResponse> regress(
        RegressionOuterClass.RegressionRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRegressMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Predict -- provides access to loaded TensorFlow model.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<Predict.PredictResponse> predict(
        Predict.PredictRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getPredictMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * MultiInference API for multi-headed models.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<Inference.MultiInferenceResponse> multiInference(
        Inference.MultiInferenceRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getMultiInferenceMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * GetModelMetadata - provides access to metadata for loaded models.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<GetModelMetadata.GetModelMetadataResponse> getModelMetadata(
        GetModelMetadata.GetModelMetadataRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetModelMetadataMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CLASSIFY = 0;
  private static final int METHODID_REGRESS = 1;
  private static final int METHODID_PREDICT = 2;
  private static final int METHODID_MULTI_INFERENCE = 3;
  private static final int METHODID_GET_MODEL_METADATA = 4;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final PredictionServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(PredictionServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CLASSIFY:
          serviceImpl.classify((Classification.ClassificationRequest) request,
              (io.grpc.stub.StreamObserver<Classification.ClassificationResponse>) responseObserver);
          break;
        case METHODID_REGRESS:
          serviceImpl.regress((RegressionOuterClass.RegressionRequest) request,
              (io.grpc.stub.StreamObserver<RegressionOuterClass.RegressionResponse>) responseObserver);
          break;
        case METHODID_PREDICT:
          serviceImpl.predict((Predict.PredictRequest) request,
              (io.grpc.stub.StreamObserver<Predict.PredictResponse>) responseObserver);
          break;
        case METHODID_MULTI_INFERENCE:
          serviceImpl.multiInference((Inference.MultiInferenceRequest) request,
              (io.grpc.stub.StreamObserver<Inference.MultiInferenceResponse>) responseObserver);
          break;
        case METHODID_GET_MODEL_METADATA:
          serviceImpl.getModelMetadata((GetModelMetadata.GetModelMetadataRequest) request,
              (io.grpc.stub.StreamObserver<GetModelMetadata.GetModelMetadataResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @Override
    @SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class PredictionServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    PredictionServiceBaseDescriptorSupplier() {}

    @Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return PredictionServiceOuterClass.getDescriptor();
    }

    @Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("PredictionService");
    }
  }

  private static final class PredictionServiceFileDescriptorSupplier
      extends PredictionServiceBaseDescriptorSupplier {
    PredictionServiceFileDescriptorSupplier() {}
  }

  private static final class PredictionServiceMethodDescriptorSupplier
      extends PredictionServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    PredictionServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (PredictionServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new PredictionServiceFileDescriptorSupplier())
              .addMethod(getClassifyMethod())
              .addMethod(getRegressMethod())
              .addMethod(getPredictMethod())
              .addMethod(getMultiInferenceMethod())
              .addMethod(getGetModelMetadataMethod())
              .build();
        }
      }
    }
    return result;
  }
}
