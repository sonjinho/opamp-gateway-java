package io.opentelemetry.opamp.owntelemetry.domain;

import lombok.Builder;

import java.util.Map;
import java.util.UUID;

@Builder
public record OwnTelemetrySetting(
        UUID id,
        String type,
        String version,
        Map<String, String> labels,
        Boolean ownMetricEnable,
        Boolean ownTraceEnable,
        Boolean ownLogEnable,
        OwnTelemetryConnectionSetting ownTraceSetting,
        OwnTelemetryConnectionSetting ownMetricSetting,
        OwnTelemetryConnectionSetting ownLogSetting
) {
    // expose id,type, labels, ownMetricEnable, ownTraceEnable, ownLogEnable
    // create new record
    public OwnTelemetrySettingSummary toSummary() {
        return new OwnTelemetrySettingSummary(id, type, version, labels, ownMetricEnable, ownTraceEnable, ownLogEnable);
    }

    public record OwnTelemetrySettingSummary(
            UUID id,
            String type,
            String version,
            Map<String, String> labels,
            Boolean ownMetricEnable,
            Boolean ownTraceEnable,
            Boolean ownLogEnable
    ) {
    }


}
