package io.opentelemetry.opamp.gateway.domain.agent;

import opamp.proto.Opamp;

public record AgentConfigFileDomain(byte[] body, String contentType) {
    public static AgentConfigFileDomain of(Opamp.AgentConfigFile value) {
        return new AgentConfigFileDomain(
                value.getBody().toByteArray(),
                value.getContentType()
        );
    }
}
