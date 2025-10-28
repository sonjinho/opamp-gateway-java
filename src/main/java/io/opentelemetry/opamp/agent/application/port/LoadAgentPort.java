package io.opentelemetry.opamp.agent.application.port;

import io.opentelemetry.opamp.agent.domain.AgentDomain;

import java.util.List;
import java.util.UUID;

public interface LoadAgentPort {
    AgentDomain loadAgent(UUID uuid);

    boolean isExist(UUID uuid);

    List<AgentDomain> loadActiveAgents();
}
