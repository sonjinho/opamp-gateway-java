package io.opentelemetry.opamp.telemetry.trace.adapter.outbound.tempo.dto;

import lombok.Builder;

@Builder
public record TempoSearchQueryDTO(String query, String tags, String minDuration, String maxDuration, String limit,
                                  Long start, Long end, Integer spss) {
}
