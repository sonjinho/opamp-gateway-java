package io.opentelemetry.opamp.telemetry.trace.adapter.outbound.tempo.dto;

public record TempoTraceMetrics(
        Integer inspectedTraces,
        String inspectedBytes,
        Integer totalBlocks
) {
}
