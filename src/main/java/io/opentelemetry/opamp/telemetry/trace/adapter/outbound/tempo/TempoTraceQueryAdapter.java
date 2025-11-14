package io.opentelemetry.opamp.telemetry.trace.adapter.outbound.tempo;

import io.opentelemetry.opamp.telemetry.trace.adapter.outbound.tempo.configuration.TempoRestClient;
import io.opentelemetry.opamp.telemetry.trace.adapter.outbound.tempo.mapper.TempoTraceMapper;
import io.opentelemetry.opamp.telemetry.trace.application.port.TraceQueryPort;
import io.opentelemetry.opamp.telemetry.trace.domain.OtelTraceRecord;
import io.opentelemetry.proto.trace.v1.TracesData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TempoTraceQueryAdapter implements TraceQueryPort {

    private final TempoRestClient tempoRestClient;
    private final TempoTraceMapper tempoTraceMapper;

    @Override
    public OtelTraceRecord getTrace(String traceId) {
        Optional<TracesData> trace = tempoRestClient.getTrace(traceId);
        return tempoTraceMapper.toOtelTraceRecord(trace);
    }
}
