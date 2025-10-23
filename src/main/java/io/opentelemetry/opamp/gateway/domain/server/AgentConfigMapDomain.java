package io.opentelemetry.opamp.gateway.domain.server;

import io.opentelemetry.opamp.gateway.domain.server.AgentConfigFileDomain;

import java.util.Map;

public record AgentConfigMapDomain(
        Map<String, AgentConfigFileDomain> configMap
) {

}
