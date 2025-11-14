package io.opentelemetry.opamp.telemetry.trace.adapter.outbound.tempo.dto;

import java.util.List;

public record TempoSearchTagValueResponseDTO(
        List<TagValue> tagValues,
        Metrics metrics
) {
    public record TagValue(String type, String value) {
    }

    public record Metrics(String inspectedBytes) {
    }
}
