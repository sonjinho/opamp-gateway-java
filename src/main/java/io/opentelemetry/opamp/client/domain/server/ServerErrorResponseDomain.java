package io.opentelemetry.opamp.client.domain.server;

public record ServerErrorResponseDomain(
        ServerErrorResponseType type,
        String errorMessage,
        RetryInfoDomain retryInfo
) {

}
