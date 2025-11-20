package io.opentelemetry.opamp.agent.application.usecase;

import io.opentelemetry.opamp.agent.application.command.SearchAgentsCommand;
import io.opentelemetry.opamp.agent.domain.AgentDomain;
import io.opentelemetry.opamp.client.application.command.ChangeAgentIdCommand;
import io.opentelemetry.opamp.client.application.command.UpdateAgentConfigCommand;
import io.opentelemetry.opamp.client.domain.agent.AgentToServerDomain;

import java.util.List;
import java.util.UUID;

public interface AgentUseCase {
    AgentDomain loadAgent(UUID uuid);

    void saveAgent(AgentToServerDomain agentToServer);

    boolean updateRemoteConfig(UpdateAgentConfigCommand command);

    boolean changeAgentId(ChangeAgentIdCommand command);

    List<AgentDomain> loadAllAgents(SearchAgentsCommand command);

    boolean restart(UUID instanceId);

    boolean changeOwnTelemetry(UUID targetId, UUID ownTelemetryId);
}
