package io.opentelemetry.opamp.receiver.adapter.inbound.web;

import io.opentelemetry.opamp.receiver.application.usecase.OpampTelemetryUseCase;
import io.opentelemetry.opamp.receiver.domain.OwnTelemetrySetting;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
public class OpampReceiverController {

    private final OpampTelemetryUseCase opampTelemetryUseCase;

    @GetMapping(value = "/api/v1/own-telemetry/settings/{uuid}")
    public OwnTelemetrySetting telemetrySetting(@PathVariable("uuid") UUID uuid) {
        return opampTelemetryUseCase.telemetrySetting(uuid);
    }
}
