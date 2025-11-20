package io.opentelemetry.opamp.client.domain.server;

public record RetryInfoDomain(
        long retryAfterNanoseconds
) {
}
