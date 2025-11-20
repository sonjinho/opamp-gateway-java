package io.opentelemetry.opamp.client.domain.server;

import java.util.UUID;

public record AgentIdentificationDomain(
    UUID newInstanceUid
) {
}