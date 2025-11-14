package io.opentelemetry.opamp.telemetry.trace.adapter.outbound.tempo;

import lombok.Builder;

@Builder
public record TempoTagValuesQuery(String start, String end, String q, Integer limit, Integer maxStaleValues) {
}
