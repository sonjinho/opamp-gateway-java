package io.opentelemetry.opamp.client.application.command;

import io.opentelemetry.opamp.client.domain.server.AgentRemoteConfigDomain;
import io.opentelemetry.opamp.client.domain.server.ServerToAgentDomain;

import java.util.UUID;

public record UpdateAgentConfigCommand(
        UUID targetId,
        AgentRemoteConfigDomain agentRemoteConfig
) implements ServerToAgentEventCommand {
    @Override
    public ServerToAgentDomain toDomain() {
        return ServerToAgentDomain.builder().instanceId(targetId).agentRemoteConfig(agentRemoteConfig).build();
    }
}
