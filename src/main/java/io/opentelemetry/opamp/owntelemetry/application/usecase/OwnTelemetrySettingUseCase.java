package io.opentelemetry.opamp.owntelemetry.application.usecase;

import io.opentelemetry.opamp.owntelemetry.application.port.SearchOwnTelemetryQuery;
import io.opentelemetry.opamp.owntelemetry.domain.OwnTelemetrySetting;

import java.util.List;
import java.util.UUID;

public interface OwnTelemetrySettingUseCase {
    OwnTelemetrySetting telemetrySetting(UUID uuid);

    List<OwnTelemetrySetting.OwnTelemetrySettingSummary> telemetrySettings(SearchOwnTelemetryQuery query);
}
