package io.opentelemetry.opamp.gateway.application.port;

import io.opentelemetry.opamp.gateway.domain.agent.AgentToServerDomain;

import java.util.UUID;

public interface LoadAgentToServerPort {
    AgentToServerDomain loadAgentToServer(UUID uuid);

    boolean isExist(UUID uuid);
}
