package io.opentelemetry.opamp.agent.adapter.inbound.web.dto;

import io.opentelemetry.opamp.gateway.domain.agent.AgentConfigFileDomain;

import java.util.Base64;

public record AgentConfigFileRequest(String body,
                                     String contentType) {
    public AgentConfigFileDomain toDomain() {
        // body base64 to byte[]
        return new AgentConfigFileDomain(Base64.getDecoder().decode(body), contentType);
    }

}
