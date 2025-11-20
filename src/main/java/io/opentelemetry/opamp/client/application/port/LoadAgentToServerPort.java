package io.opentelemetry.opamp.client.application.port;

import io.opentelemetry.opamp.client.domain.agent.AgentToServerDomain;

import java.util.UUID;

public interface LoadAgentToServerPort {
    AgentToServerDomain loadAgentToServer(UUID uuid);

    boolean isExist(UUID uuid);
}
