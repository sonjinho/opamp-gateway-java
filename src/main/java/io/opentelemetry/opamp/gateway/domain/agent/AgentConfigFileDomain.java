package io.opentelemetry.opamp.gateway.domain.agent;

import opamp.proto.Opamp;

public record AgentConfigFileDomain(byte[] body, String contentType) {
}
