package io.opentelemetry.opamp.owntelemetry.adapter.inbound.web;

import io.opentelemetry.opamp.owntelemetry.application.port.SearchOwnTelemetryQuery;
import io.opentelemetry.opamp.owntelemetry.application.usecase.OwnTelemetrySettingUseCase;
import io.opentelemetry.opamp.owntelemetry.domain.OwnTelemetrySetting;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
public class OwnTelemetrySettingController {

    private final OwnTelemetrySettingUseCase ownTelemetrySettingUseCase;

    @GetMapping(value = "/api/v1/own-telemetry/settings/{uuid}")
    public OwnTelemetrySetting telemetrySetting(@PathVariable("uuid") UUID uuid) {
        return ownTelemetrySettingUseCase.telemetrySetting(uuid);
    }

    @GetMapping(value = "/api/v1/own-telemetry/settings")
    public List<OwnTelemetrySetting.OwnTelemetrySettingSummary> telemetrySettings(@ModelAttribute SearchOwnTelemetryQuery query) {
        return ownTelemetrySettingUseCase.telemetrySettings(query);
    }
}
