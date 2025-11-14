package io.opentelemetry.opamp.telemetry.trace.adapter.outbound.tempo.dto;

import java.util.List;

public record TempoSearchTageNameResponseDTO(
        List<TempoSearchTageNameScope> scopes
) {
    public record TempoSearchTageNameScope(String name, List<String> tags) {
    }
}
