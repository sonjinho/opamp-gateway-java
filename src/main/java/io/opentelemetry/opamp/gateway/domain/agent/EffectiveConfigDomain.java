package io.opentelemetry.opamp.gateway.domain.agent;

import java.util.Map;

public record EffectiveConfigDomain(Map<String, AgentConfigFileDomain> configMap) {
}

