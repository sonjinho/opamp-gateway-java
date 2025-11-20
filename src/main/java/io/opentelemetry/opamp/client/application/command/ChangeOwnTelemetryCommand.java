package io.opentelemetry.opamp.client.application.command;

import io.opentelemetry.opamp.client.domain.server.ConnectionSettingsOffersDomain;
import io.opentelemetry.opamp.client.domain.server.ServerToAgentDomain;
import io.opentelemetry.opamp.client.domain.server.ServerToAgentFlags;
import io.opentelemetry.opamp.owntelemetry.domain.OwnTelemetrySetting;

import java.util.UUID;

public record ChangeOwnTelemetryCommand(
        UUID targetId,
        OwnTelemetrySetting connectionSetting
) implements ServerToAgentEventCommand {
    @Override
    public ServerToAgentDomain toDomain() {
        return ServerToAgentDomain.builder()
                .instanceId(targetId)
                .flags(ServerToAgentFlags.UNSPECIFIED.val())
                .connectionSettingsOffers(
                        ConnectionSettingsOffersDomain.from(connectionSetting)
                )
                .build();
    }
}
