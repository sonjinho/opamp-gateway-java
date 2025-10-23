package io.opentelemetry.opamp.gateway.domain.server;

import java.util.Map;

public record ConnectionSettingsOffersDomain(
        byte[] hash,
        OpAMPConnectionSettingsDomain opamp,
        TelemetryConnectionSettingsDomain ownMetrics,
        TelemetryConnectionSettingsDomain ownTraces,
        TelemetryConnectionSettingsDomain ownLogs,
        Map<String, OtherConnectionSettingsDomain> otherConnections
) {
}
