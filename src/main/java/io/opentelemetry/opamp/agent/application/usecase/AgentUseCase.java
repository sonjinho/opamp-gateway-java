package io.opentelemetry.opamp.agent.application.usecase;

import io.opentelemetry.opamp.agent.application.command.SearchAgentsCommand;
import io.opentelemetry.opamp.agent.application.command.UpdateAgentConfigCommand;
import io.opentelemetry.opamp.agent.domain.AgentDomain;
import io.opentelemetry.opamp.client.domain.agent.AgentToServerDomain;
import io.opentelemetry.opamp.client.domain.server.ServerToAgentDomain;

import java.util.List;
import java.util.UUID;

public interface AgentUseCase {
    AgentDomain loadAgent(UUID uuid);

    void saveAgent(AgentToServerDomain agentToServer);

    default void updateRemoteConfig(UpdateAgentConfigCommand command) {
    }

    ;

    void updateAgent(ServerToAgentDomain serverToAgentDomain);

    Long requestFlag(UUID uuid);

    List<AgentDomain> loadAllAgents(SearchAgentsCommand command);

}
