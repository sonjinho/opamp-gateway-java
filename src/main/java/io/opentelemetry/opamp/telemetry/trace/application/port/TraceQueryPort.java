package io.opentelemetry.opamp.telemetry.trace.application.port;

import io.opentelemetry.opamp.telemetry.trace.domain.OtelTraceRecord;

public interface TraceQueryPort {
    OtelTraceRecord getTrace(String traceId);
}
