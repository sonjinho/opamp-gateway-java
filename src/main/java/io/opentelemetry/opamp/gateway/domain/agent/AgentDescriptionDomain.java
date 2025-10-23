package io.opentelemetry.opamp.gateway.domain.agent;

import opamp.proto.Anyvalue;
import opamp.proto.Opamp;

import java.util.HashMap;
import java.util.Map;

public record AgentDescriptionDomain(Map<String, Anyvalue.AnyValue> identifyingAttributes,
                                     Map<String, Anyvalue.AnyValue> nonIdentifyingAttributes) {
    public static AgentDescriptionDomain of(Opamp.AgentDescription description) {
        if (description == null) {
            return null;
        }
        Map<String, Anyvalue.AnyValue> identifyingAttributes = new HashMap<>();
        for (Anyvalue.KeyValue kv : description.getIdentifyingAttributesList()) {
            identifyingAttributes.put(kv.getKey(), kv.getValue());
        }

        Map<String, Anyvalue.AnyValue> nonIdentifyingAttributes = new HashMap<>();
        for (Anyvalue.KeyValue kv : description.getNonIdentifyingAttributesList()) {
            nonIdentifyingAttributes.put(kv.getKey(), kv.getValue());
        }
        return new AgentDescriptionDomain(identifyingAttributes, nonIdentifyingAttributes);
    }

}
