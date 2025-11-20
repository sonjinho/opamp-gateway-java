package io.opentelemetry.opamp.client.domain.server;

public record ProxyConnectionSettingsDomain(
        String url,
        HeadersDomain connectHeaders
) {
}
