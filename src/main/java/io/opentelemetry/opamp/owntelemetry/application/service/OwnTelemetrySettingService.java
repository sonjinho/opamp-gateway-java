package io.opentelemetry.opamp.owntelemetry.application.service;

import io.opentelemetry.opamp.client.application.port.AgentPushPort;
import io.opentelemetry.opamp.owntelemetry.application.port.LoadOwnTelemetrySettingPort;
import io.opentelemetry.opamp.owntelemetry.application.port.SearchOwnTelemetryQuery;
import io.opentelemetry.opamp.owntelemetry.application.port.UpdateOwnTelemetrySettingPort;
import io.opentelemetry.opamp.owntelemetry.application.usecase.OwnTelemetrySettingUseCase;
import io.opentelemetry.opamp.owntelemetry.domain.OwnTelemetrySetting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OwnTelemetrySettingService implements OwnTelemetrySettingUseCase {

    private final LoadOwnTelemetrySettingPort loadPort;
    private final UpdateOwnTelemetrySettingPort updatePort;

    private final AgentPushPort agentPushPort;

    @Override
    public OwnTelemetrySetting telemetrySetting(UUID uuid) {
        return loadPort.loadOwnTelemetry(uuid);
    }

    @Override
    public List<OwnTelemetrySetting.OwnTelemetrySettingSummary> telemetrySettings(SearchOwnTelemetryQuery query) {
        return loadPort.searchOwnTelemetry(query);
    }

    public OwnTelemetrySetting.OwnTelemetrySettingSummary save(OwnTelemetrySetting setting) {
        return updatePort.save(setting);
    }

    public OwnTelemetrySetting.OwnTelemetrySettingSummary update(OwnTelemetrySetting setting) {
        var result = updatePort.update(setting);
        var telemetrySetting = loadPort.loadOwnTelemetry(result.id());
        agentPushPort.batchUpdateOwnTelemetrySetting(telemetrySetting);
        return result;
    }

}
