package io.opentelemetry.opamp.gateway.domain.server;

public record TelemetryConnectionSettingsDomain(
    String destinationEndpoint,
    HeadersDomain headers,
    TlsCertificateDomain certificate,
    TlsConnectionSettingsDomain tls,
    ProxyConnectionSettingsDomain proxy
) {
}
