package io.opentelemetry.opamp.receiver.application.usecase;

import io.opentelemetry.opamp.receiver.domain.OwnTelemetrySetting;

import java.util.UUID;

public interface OpampTelemetryUseCase {
    OwnTelemetrySetting telemetrySetting(UUID uuid);
}
