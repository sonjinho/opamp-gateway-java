package io.opentelemetry.opamp.receiver.metric;

import io.grpc.stub.StreamObserver;
import io.opentelemetry.proto.collector.metrics.v1.ExportMetricsServiceRequest;
import io.opentelemetry.proto.collector.metrics.v1.ExportMetricsServiceResponse;
import io.opentelemetry.proto.collector.metrics.v1.MetricsServiceGrpc;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class OTELMeticServiceGrpcImpl extends MetricsServiceGrpc.MetricsServiceImplBase {

    @Override
    public void export(ExportMetricsServiceRequest request, StreamObserver<ExportMetricsServiceResponse> responseObserver) {
        super.export(request, responseObserver);
    }
}
