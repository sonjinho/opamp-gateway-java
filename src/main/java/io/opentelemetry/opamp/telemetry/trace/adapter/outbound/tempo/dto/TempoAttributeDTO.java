package io.opentelemetry.opamp.telemetry.trace.adapter.outbound.tempo.dto;

public record TempoAttributeDTO(
        String key,
        TempoAttributeValueDTO value
) {
}
