package io.opentelemetry.opamp.receiver.adapter.inbound.grpc.metric;

import io.grpc.stub.StreamObserver;
import io.opentelemetry.proto.collector.metrics.v1.ExportMetricsServiceRequest;
import io.opentelemetry.proto.collector.metrics.v1.ExportMetricsServiceResponse;
import io.opentelemetry.proto.collector.metrics.v1.MetricsServiceGrpc;
import io.opentelemetry.proto.metrics.v1.ScopeMetrics;
import io.opentelemetry.proto.resource.v1.Resource;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@GrpcService
public class OTELMeticServiceGrpcImpl extends MetricsServiceGrpc.MetricsServiceImplBase {

    @Override
    public void export(ExportMetricsServiceRequest request, StreamObserver<ExportMetricsServiceResponse> responseObserver) {
        Map<Resource, List<ScopeMetrics>> map = new HashMap<>();
        for (var resourceMetrics : request.getResourceMetricsList()) {
            if (map.containsKey(resourceMetrics.getResource())) {
                map.get(resourceMetrics.getResource()).addAll(resourceMetrics.getScopeMetricsList());
            } else {
                map.put(resourceMetrics.getResource(), resourceMetrics.getScopeMetricsList());
            }
        }
        responseObserver.onCompleted();
    }
}
