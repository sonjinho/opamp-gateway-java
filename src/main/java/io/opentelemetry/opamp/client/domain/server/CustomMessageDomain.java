package io.opentelemetry.opamp.client.domain.server;

public record CustomMessageDomain(
        String capability,
        String type,
        byte[] data
) {
}
