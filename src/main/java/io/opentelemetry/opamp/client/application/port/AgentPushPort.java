package io.opentelemetry.opamp.client.application.port;

import io.opentelemetry.opamp.client.domain.server.ServerToAgentDomain;
import io.opentelemetry.opamp.owntelemetry.domain.OwnTelemetrySetting;

import java.util.UUID;

public interface AgentPushPort {
    void push(UUID agentId, ServerToAgentDomain serverToAgentDomain);

    void batchUpdateOwnTelemetrySetting(OwnTelemetrySetting telemetrySetting);
}
