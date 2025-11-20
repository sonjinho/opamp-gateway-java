package io.opentelemetry.opamp.client.domain.agent;

import java.util.Map;

public record AvailableComponentsDomain(
        byte[] hash,
        Map<String, ComponentDetailsDomain> components
) {
    public record ComponentDetailsDomain(Map<String, String> metadata,
                                         Map<String, ComponentDetailsDomain> subComponentMap) {
    }
}
