package io.opentelemetry.opamp.gateway.domain.server;

import java.util.UUID;

public record AgentIdentificationDomain(
    UUID newInstanceUid
) {
}