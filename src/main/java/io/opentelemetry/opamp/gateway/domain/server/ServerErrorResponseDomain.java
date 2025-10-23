package io.opentelemetry.opamp.gateway.domain.server;

public record ServerErrorResponseDomain(
        ServerErrorResponseType type,
        String errorMessage,
        RetryInfoDomain retryInfo
) {

}
