package io.opentelemetry.opamp.gateway.domain.server;

public record TlsCertificateDomain(
        byte[] cert,
        byte[] privateKey,
        byte[] caCert
) {
}
