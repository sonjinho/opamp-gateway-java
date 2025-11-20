package io.opentelemetry.opamp.owntelemetry.domain;

import io.opentelemetry.opamp.client.domain.server.HeadersDomain;
import io.opentelemetry.opamp.client.domain.server.ProxyConnectionSettingsDomain;
import io.opentelemetry.opamp.client.domain.server.TlsCertificateDomain;
import io.opentelemetry.opamp.client.domain.server.TlsConnectionSettingsDomain;

public record OwnTelemetryConnectionSetting(
        Integer id,
        String destinationEndpoint,
        HeadersDomain headers,
        TlsCertificateDomain certificate,
        TlsConnectionSettingsDomain tls,
        ProxyConnectionSettingsDomain proxy
) {
}