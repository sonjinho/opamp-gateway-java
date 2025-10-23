package io.opentelemetry.opamp.gateway.domain.server;

import java.util.Map;

public record OtherConnectionSettingsDomain(
    String destinationEndpoint,
    HeadersDomain headers,
    TlsCertificateDomain certificate,
    Map<String, String> otherSettings,
    TlsConnectionSettingsDomain tls,
    ProxyConnectionSettingsDomain proxy
) {
}
