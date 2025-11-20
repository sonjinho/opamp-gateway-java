package io.opentelemetry.opamp.client.application.command;

import io.opentelemetry.opamp.client.domain.server.AgentRemoteConfigDomain;
import io.opentelemetry.opamp.client.domain.server.ServerToAgentDomain;

import java.util.UUID;

public record UpdateAgentConfigCommand(
        UUID agentId,
        AgentRemoteConfigDomain agentRemoteConfig
) implements ServerToAgentEventCommand {
    @Override
    public UUID targetId() {
        return agentId;
    }

    @Override
    public ServerToAgentDomain toDomain() {
        return ServerToAgentDomain.builder().instanceId(agentId).agentRemoteConfig(agentRemoteConfig).build();
    }
}
