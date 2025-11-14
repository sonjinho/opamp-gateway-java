package io.opentelemetry.opamp.telemetry.trace.adapter.outbound.tempo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TempoTraceSummaryDTO(@JsonProperty("traceID") String traceID,
                                   @JsonProperty("rootServiceName") String rootServiceName,
                                   @JsonProperty("rootTraceName") String rootTraceName,
                                   @JsonProperty("startTimeUnixNano") String startTimeUnixNano,
                                   @JsonProperty("durationMs") long durationMs,
                                   @JsonProperty("spanSets") List<TempoSpanSetDTO> spanSets) {
}
