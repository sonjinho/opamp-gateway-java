package io.opentelemetry.opamp.agent.adapter.inbound.web.dto;

import java.util.UUID;

public record RequestChangeOwnTelemetryDTO(
        UUID ownTelemetryId
) {
}
