package io.opentelemetry.opamp.agent.application.command;

import io.opentelemetry.opamp.gateway.domain.server.OtherConnectionSettingsDomain;
import io.opentelemetry.opamp.gateway.domain.server.TelemetryConnectionSettingsDomain;

import java.util.Map;
import java.util.UUID;

public record UpdateConnectionsSettingCommand(
        UUID instanceId,
        TelemetryConnectionSettingsDomain ownMetrics,
        TelemetryConnectionSettingsDomain ownTraces,
        TelemetryConnectionSettingsDomain ownLogs,
        Map<String, OtherConnectionSettingsDomain> otherConnections
) {
}
