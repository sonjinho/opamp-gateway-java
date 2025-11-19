package io.opentelemetry.opamp.agent.adapter.inbound.web.dto;

import io.opentelemetry.opamp.gateway.domain.server.AgentConfigMapDomain;
import io.opentelemetry.opamp.gateway.domain.server.AgentRemoteConfigDomain;

import java.nio.ByteBuffer;

public record AgentRemoteConfigDTO(AgentConfigMapRequest config) {
    public AgentRemoteConfigDomain toDomain() {
        AgentConfigMapDomain domain = config.toDomain();
        int hash = domain.configMap().values().stream().toList().hashCode();
        return new AgentRemoteConfigDomain(config.toDomain(), ByteBuffer.allocate(4).putInt(hash).array());
    }
}
