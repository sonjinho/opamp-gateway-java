package io.opentelemetry.opamp.client.domain.server;

import io.opentelemetry.opamp.client.domain.agent.AgentConfigFileDomain;

import java.util.Map;

public record AgentConfigMapDomain(
        Map<String, AgentConfigFileDomain> configMap
) {

}
