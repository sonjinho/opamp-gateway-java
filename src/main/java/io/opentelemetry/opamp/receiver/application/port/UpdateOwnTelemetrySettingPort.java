package io.opentelemetry.opamp.receiver.application.port;

import io.opentelemetry.opamp.receiver.domain.OwnTelemetrySetting;

import java.util.UUID;

public interface UpdateOwnTelemetrySettingPort {
    OwnTelemetrySetting.OwnTelemetrySettingSummary save(OwnTelemetrySetting ownTelemetrySetting);

    OwnTelemetrySetting.OwnTelemetrySettingSummary update(OwnTelemetrySetting ownTelemetrySetting);

    UUID delete(UUID uuid);
}
