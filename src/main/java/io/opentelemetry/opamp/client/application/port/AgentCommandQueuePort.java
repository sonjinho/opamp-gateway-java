package io.opentelemetry.opamp.client.application.port;

import io.opentelemetry.opamp.client.application.command.ServerToAgentEventCommand;
import io.opentelemetry.opamp.client.domain.server.ServerToAgentDomain;

import java.util.Optional;
import java.util.UUID;

public interface AgentCommandQueuePort {
    boolean enqueue(ServerToAgentEventCommand event);

    Optional<ServerToAgentDomain> pollNextCommand(UUID uuid);
}
