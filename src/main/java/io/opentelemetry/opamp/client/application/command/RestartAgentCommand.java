package io.opentelemetry.opamp.client.application.command;

import io.opentelemetry.opamp.client.domain.server.CommandType;
import io.opentelemetry.opamp.client.domain.server.ServerToAgentCommandDomain;
import io.opentelemetry.opamp.client.domain.server.ServerToAgentDomain;
import io.opentelemetry.opamp.client.domain.server.ServerToAgentFlags;

import java.util.UUID;

public record RestartAgentCommand(UUID targetId) implements ServerToAgentEventCommand {
    @Override
    public ServerToAgentDomain toDomain() {
        return ServerToAgentDomain.builder()
                .instanceId(targetId)
                .flags(ServerToAgentFlags.UNSPECIFIED.val())
                .command(new ServerToAgentCommandDomain(CommandType.RESTART))
                .build();
    }
}
