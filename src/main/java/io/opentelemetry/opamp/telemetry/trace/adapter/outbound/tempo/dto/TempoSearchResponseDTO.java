package io.opentelemetry.opamp.telemetry.trace.adapter.outbound.tempo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TempoSearchResponseDTO(@JsonProperty("traces") List<TempoTraceSummaryDTO> traces,
                                     @JsonProperty("metrics") TempoTraceMetrics metrics) {
}
