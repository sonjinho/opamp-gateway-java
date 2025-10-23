package io.opentelemetry.opamp.gateway.domain.agent;

import opamp.proto.Anyvalue;
import opamp.proto.Opamp;

import java.util.HashMap;
import java.util.Map;

public record AgentDescriptionDomain(Map<String, Anyvalue.AnyValue> identifyingAttributes,
                                     Map<String, Anyvalue.AnyValue> nonIdentifyingAttributes) {
}
