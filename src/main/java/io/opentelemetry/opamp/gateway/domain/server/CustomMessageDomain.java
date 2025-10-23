package io.opentelemetry.opamp.gateway.domain.server;

public record CustomMessageDomain(
        String capability,
        String type,
        byte[] data
) {
}
