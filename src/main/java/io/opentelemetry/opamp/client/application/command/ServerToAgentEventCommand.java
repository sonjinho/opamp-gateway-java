package io.opentelemetry.opamp.client.application.command;

import io.opentelemetry.opamp.client.domain.server.ServerToAgentDomain;

import java.util.UUID;

public interface ServerToAgentEventCommand {
    UUID targetId();

    ServerToAgentDomain toDomain();
}
