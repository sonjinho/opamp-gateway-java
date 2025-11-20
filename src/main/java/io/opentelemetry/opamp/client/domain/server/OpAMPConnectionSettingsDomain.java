package io.opentelemetry.opamp.client.domain.server;

public record OpAMPConnectionSettingsDomain(
        String destinationEndpoint,
        HeadersDomain headers,
        TlsCertificateDomain certificate,
        Long heartbeatIntervalSeconds,
        TlsConnectionSettingsDomain tls,
        ProxyConnectionSettingsDomain proxy
) {
}
