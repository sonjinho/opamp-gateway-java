package io.opentelemetry.opamp.receiver.application.service;

import io.opentelemetry.opamp.receiver.application.port.LoadOwnTelemetrySettingPort;
import io.opentelemetry.opamp.receiver.application.port.UpdateOwnTelemetrySettingPort;
import io.opentelemetry.opamp.receiver.application.usecase.OpampTelemetryUseCase;
import io.opentelemetry.opamp.receiver.domain.OwnTelemetrySetting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OpampTelemetryService implements OpampTelemetryUseCase {

    private final LoadOwnTelemetrySettingPort loadPort;
    private final UpdateOwnTelemetrySettingPort updatePort;

    @Override
    public OwnTelemetrySetting telemetrySetting(UUID uuid) {
        return loadPort.loadOwnTelemetry(uuid);
    }
}
