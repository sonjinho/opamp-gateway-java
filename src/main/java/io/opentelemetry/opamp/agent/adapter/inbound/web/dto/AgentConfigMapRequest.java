package io.opentelemetry.opamp.agent.adapter.inbound.web.dto;

import io.opentelemetry.opamp.client.domain.server.AgentConfigMapDomain;

import java.util.Map;
import java.util.stream.Collectors;

public record AgentConfigMapRequest(Map<String, AgentConfigFileRequest> configMap) {
    public AgentConfigMapDomain toDomain() {
        return new AgentConfigMapDomain(configMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toDomain())));
    }
}
