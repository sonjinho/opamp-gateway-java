package io.opentelemetry.opamp.telemetry.trace.adapter.outbound.tempo;

import lombok.Builder;

@Builder
public record TempoTagNamesQuery(String scope, String q, String start, String end, Integer limit,
                                 Integer maxStaleValues) {
}
