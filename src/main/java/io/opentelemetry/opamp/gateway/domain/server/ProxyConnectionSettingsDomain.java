package io.opentelemetry.opamp.gateway.domain.server;

public record ProxyConnectionSettingsDomain(
        String url,
        HeadersDomain connectHeaders
) {
}
