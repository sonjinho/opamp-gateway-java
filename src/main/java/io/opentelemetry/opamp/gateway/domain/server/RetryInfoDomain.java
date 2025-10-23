package io.opentelemetry.opamp.gateway.domain.server;

public record RetryInfoDomain(
        long retryAfterNanoseconds
) {
}
