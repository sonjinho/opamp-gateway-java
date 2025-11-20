package io.opentelemetry.opamp.client.domain.server;

import io.opentelemetry.opamp.common.util.UUIDUtil;
import io.opentelemetry.opamp.owntelemetry.domain.OwnTelemetrySetting;

import java.util.Map;

public record ConnectionSettingsOffersDomain(
        byte[] hash,
        OpAMPConnectionSettingsDomain opamp,
        TelemetryConnectionSettingsDomain ownMetrics,
        TelemetryConnectionSettingsDomain ownTraces,
        TelemetryConnectionSettingsDomain ownLogs,
        Map<String, OtherConnectionSettingsDomain> otherConnections
) {
    public static ConnectionSettingsOffersDomain from(OwnTelemetrySetting setting) {
        byte[] hash = UUIDUtil.INSTANCE.convertUUIDToBytes(setting.id());

        return new ConnectionSettingsOffersDomain(
                hash,
                null,
                setting.ownMetricEnable() ? TelemetryConnectionSettingsDomain.from(setting.ownMetricSetting()) : null,
                setting.ownTraceEnable() ? TelemetryConnectionSettingsDomain.from(setting.ownTraceSetting()) : null,
                setting.ownLogEnable() ? TelemetryConnectionSettingsDomain.from(setting.ownLogSetting()) : null,
                Map.of()
        );
    }
}
