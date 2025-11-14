package io.opentelemetry.opamp.telemetry.trace.adapter.outbound.tempo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TempoResponseDTO<T>(@JsonProperty("status") String status, @JsonProperty("data") T data) {
}
