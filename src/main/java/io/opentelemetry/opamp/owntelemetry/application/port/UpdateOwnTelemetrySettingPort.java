package io.opentelemetry.opamp.owntelemetry.application.port;

import io.opentelemetry.opamp.owntelemetry.domain.OwnTelemetrySetting;

import java.util.UUID;

public interface UpdateOwnTelemetrySettingPort {
    OwnTelemetrySetting.OwnTelemetrySettingSummary save(OwnTelemetrySetting ownTelemetrySetting);

    OwnTelemetrySetting.OwnTelemetrySettingSummary update(OwnTelemetrySetting ownTelemetrySetting);

    UUID delete(UUID uuid);
}
