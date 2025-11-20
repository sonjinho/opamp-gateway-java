package io.opentelemetry.opamp.client.domain.agent;

import java.util.Map;

public record EffectiveConfigDomain(Map<String, AgentConfigFileDomain> configMap) {
    public static EffectiveConfigDomain merge(EffectiveConfigDomain src, EffectiveConfigDomain target) {
        if (src == null) {
            return target;
        }
        if (target == null) {
            return src;
        }
        Map<String, AgentConfigFileDomain> configMap = src.configMap;
        configMap.putAll(target.configMap);
        return new EffectiveConfigDomain(configMap);

    }
}

