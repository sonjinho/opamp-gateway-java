package io.opentelemetry.opamp.receiver.application.port;

import io.opentelemetry.opamp.receiver.domain.OwnTelemetrySetting;

import java.util.List;
import java.util.UUID;

public interface LoadOwnTelemetrySettingPort {
    OwnTelemetrySetting loadOwnTelemetry(UUID uuid);

    List<OwnTelemetrySetting.OwnTelemetrySettingSummary> searchOwnTelemetry(SearchOwnTelemetryQuery query);
}
