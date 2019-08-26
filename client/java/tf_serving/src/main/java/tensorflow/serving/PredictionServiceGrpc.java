package tensorflow.serving;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 * <pre>
 * PredictionService provides access to machine-learned models loaded by
 * model_servers.
 * </pre>
 */
@javax.annotation.Generated(
        value = "by gRPC proto compiler (version 1.1.0-SNAPSHOT)",
        comments = "Source: prediction_service.proto")
public class PredictionServiceGrpc {

    private PredictionServiceGrpc() {}

    public static final String SERVICE_NAME = "tensorflow.serving.PredictionService";

    // Static method descriptors that strictly reflect the proto.
    @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
    public static final io.grpc.MethodDescriptor<tensorflow.serving.Predict.PredictRequest,
            tensorflow.serving.Predict.PredictResponse> METHOD_PREDICT =
            io.grpc.MethodDescriptor.create(
                    io.grpc.MethodDescriptor.MethodType.UNARY,
                    generateFullMethodName(
                            "tensorflow.serving.PredictionService", "Predict"),
                    io.grpc.protobuf.ProtoUtils.marshaller(tensorflow.serving.Predict.PredictRequest.getDefaultInstance()),
                    io.grpc.protobuf.ProtoUtils.marshaller(tensorflow.serving.Predict.PredictResponse.getDefaultInstance()));

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
     * Creates a new ListenableFuture-style stub that supports unary and streaming output calls on the service
     */
    public static PredictionServiceFutureStub newFutureStub(
            io.grpc.Channel channel) {
        return new PredictionServiceFutureStub(channel);
    }

    /**
     * <pre>
     * PredictionService provides access to machine-learned models loaded by
     * model_servers.
     * </pre>
     */
    public static abstract class PredictionServiceImplBase implements io.grpc.BindableService {

        /**
         * <pre>
         * Predict -- provides access to loaded TensorFlow model.
         * </pre>
         */
        public void predict(tensorflow.serving.Predict.PredictRequest request,
                            io.grpc.stub.StreamObserver<tensorflow.serving.Predict.PredictResponse> responseObserver) {
            asyncUnimplementedUnaryCall(METHOD_PREDICT, responseObserver);
        }

        public io.grpc.ServerServiceDefinition bindService() {
            return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
                    .addMethod(
                            METHOD_PREDICT,
                            asyncUnaryCall(
                                    new MethodHandlers<
                                            tensorflow.serving.Predict.PredictRequest,
                                            tensorflow.serving.Predict.PredictResponse>(
                                            this, METHODID_PREDICT)))
                    .build();
        }
    }

    /**
     * <pre>
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

        @java.lang.Override
        protected PredictionServiceStub build(io.grpc.Channel channel,
                                              io.grpc.CallOptions callOptions) {
            return new PredictionServiceStub(channel, callOptions);
        }

        /**
         * <pre>
         * Predict -- provides access to loaded TensorFlow model.
         * </pre>
         */
        public void predict(tensorflow.serving.Predict.PredictRequest request,
                            io.grpc.stub.StreamObserver<tensorflow.serving.Predict.PredictResponse> responseObserver) {
            asyncUnaryCall(
                    getChannel().newCall(METHOD_PREDICT, getCallOptions()), request, responseObserver);
        }
    }

    /**
     * <pre>
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

        @java.lang.Override
        protected PredictionServiceBlockingStub build(io.grpc.Channel channel,
                                                      io.grpc.CallOptions callOptions) {
            return new PredictionServiceBlockingStub(channel, callOptions);
        }

        /**
         * <pre>
         * Predict -- provides access to loaded TensorFlow model.
         * </pre>
         */
        public tensorflow.serving.Predict.PredictResponse predict(tensorflow.serving.Predict.PredictRequest request) {
            return blockingUnaryCall(
                    getChannel(), METHOD_PREDICT, getCallOptions(), request);
        }
    }

    /**
     * <pre>
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

        @java.lang.Override
        protected PredictionServiceFutureStub build(io.grpc.Channel channel,
                                                    io.grpc.CallOptions callOptions) {
            return new PredictionServiceFutureStub(channel, callOptions);
        }

        /**
         * <pre>
         * Predict -- provides access to loaded TensorFlow model.
         * </pre>
         */
        public com.google.common.util.concurrent.ListenableFuture<tensorflow.serving.Predict.PredictResponse> predict(
                tensorflow.serving.Predict.PredictRequest request) {
            return futureUnaryCall(
                    getChannel().newCall(METHOD_PREDICT, getCallOptions()), request);
        }
    }

    private static final int METHODID_PREDICT = 0;

    private static class MethodHandlers<Req, Resp> implements
            io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
        private final PredictionServiceImplBase serviceImpl;
        private final int methodId;

        public MethodHandlers(PredictionServiceImplBase serviceImpl, int methodId) {
            this.serviceImpl = serviceImpl;
            this.methodId = methodId;
        }

        @java.lang.SuppressWarnings("unchecked")
        public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                case METHODID_PREDICT:
                    serviceImpl.predict((tensorflow.serving.Predict.PredictRequest) request,
                            (io.grpc.stub.StreamObserver<tensorflow.serving.Predict.PredictResponse>) responseObserver);
                    break;
                default:
                    throw new AssertionError();
            }
        }

        @java.lang.SuppressWarnings("unchecked")
        public io.grpc.stub.StreamObserver<Req> invoke(
                io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                default:
                    throw new AssertionError();
            }
        }
    }

    private static final class PredictionServiceDescriptorSupplier implements tensorflow.serving.ProtoFileDescriptorSupplier {
        public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
            return tensorflow.serving.PredictionServiceOuterClass.getDescriptor();
        }
    }

    private static io.grpc.ServiceDescriptor serviceDescriptor;

    public static synchronized io.grpc.ServiceDescriptor getServiceDescriptor() {
        if (serviceDescriptor == null) {
            serviceDescriptor = new io.grpc.ServiceDescriptor(SERVICE_NAME, METHOD_PREDICT);
        }

        return serviceDescriptor;
    }
}
