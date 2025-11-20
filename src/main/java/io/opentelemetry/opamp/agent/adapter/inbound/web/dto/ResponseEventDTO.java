package io.opentelemetry.opamp.agent.adapter.inbound.web.dto;

public record ResponseEventDTO(
        Boolean status,
        String errorMessage
) {
}
