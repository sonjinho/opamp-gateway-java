package io.opentelemetry.opamp.agent.application.usecase;

import io.opentelemetry.opamp.agent.domain.AgentDomain;
import io.opentelemetry.opamp.gateway.domain.agent.AgentToServerDomain;
import io.opentelemetry.opamp.gateway.domain.server.ServerToAgentDomain;

import java.util.UUID;

public interface AgentUseCase {
    AgentDomain loadAgent(UUID uuid);

    boolean saveAgent(AgentToServerDomain agentToServer);

    void updateAgent(ServerToAgentDomain serverToAgentDomain);
}
