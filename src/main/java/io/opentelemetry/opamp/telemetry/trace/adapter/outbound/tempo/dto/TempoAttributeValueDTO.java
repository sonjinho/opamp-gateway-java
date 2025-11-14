package io.opentelemetry.opamp.telemetry.trace.adapter.outbound.tempo.dto;

public record TempoAttributeValueDTO(
        String stringValue,
        Boolean boolValue,
        Integer intValue,
        Double doubleValue
) {
}
