package io.opentelemetry.opamp.client.domain.agent;

import java.util.Map;

public record AgentDescriptionDomain(Map<String, String> identifyingAttributes,
                                     Map<String, String> nonIdentifyingAttributes) {
    public void merge(AgentDescriptionDomain description) {
        identifyingAttributes.putAll(description.identifyingAttributes);
        nonIdentifyingAttributes.putAll(description.nonIdentifyingAttributes);
    }
}
