package io.opentelemetry.opamp.receiver.domain;

import lombok.Builder;

import java.util.Map;
import java.util.UUID;

@Builder
public record OwnTelemetrySetting(
        UUID id,
        String type,
        Map<String, String> labels,
        Boolean ownMetricEnable,
        Boolean ownTraceEnable,
        Boolean ownLogEnable,
        OwnTelemetryConnectionSetting ownTraceSetting,
        OwnTelemetryConnectionSetting ownMetricSetting,
        OwnTelemetryConnectionSetting ownLogSetting
) {
}
