package io.opentelemetry.opamp.client.application.command;

import io.opentelemetry.opamp.client.domain.server.AgentIdentificationDomain;
import io.opentelemetry.opamp.client.domain.server.ServerToAgentDomain;
import io.opentelemetry.opamp.client.domain.server.ServerToAgentFlags;

import java.util.UUID;

public record ChangeAgentIdCommand(UUID agentId, UUID newAgentId) implements ServerToAgentEventCommand {
    @Override
    public UUID targetId() {
        return agentId;
    }

    @Override
    public ServerToAgentDomain toDomain() {
        return ServerToAgentDomain.builder()
                .instanceId(agentId)
                .flags(ServerToAgentFlags.UNSPECIFIED.val())
                .agentIdentification(new AgentIdentificationDomain(newAgentId))
                .build();
    }
}
