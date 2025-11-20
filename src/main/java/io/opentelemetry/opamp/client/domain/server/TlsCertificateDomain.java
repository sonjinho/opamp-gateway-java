package io.opentelemetry.opamp.client.domain.server;

public record TlsCertificateDomain(
        byte[] cert,
        byte[] privateKey,
        byte[] caCert
) {
}
