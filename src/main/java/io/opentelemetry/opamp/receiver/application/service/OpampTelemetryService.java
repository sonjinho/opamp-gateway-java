package io.opentelemetry.opamp.receiver.application.service;

import io.opentelemetry.opamp.receiver.application.usecase.OpampTelemetryUseCase;
import io.opentelemetry.opamp.receiver.domain.OwnTelemetrySetting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OpampTelemetryService implements OpampTelemetryUseCase {

    @Override
    public OwnTelemetrySetting telemetrySetting(UUID uuid) {

        return null;
    }
}
