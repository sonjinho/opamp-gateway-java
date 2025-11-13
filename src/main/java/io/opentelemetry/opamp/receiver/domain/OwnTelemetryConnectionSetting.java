package io.opentelemetry.opamp.receiver.domain;

import io.opentelemetry.opamp.gateway.domain.server.HeadersDomain;
import io.opentelemetry.opamp.gateway.domain.server.ProxyConnectionSettingsDomain;
import io.opentelemetry.opamp.gateway.domain.server.TlsCertificateDomain;
import io.opentelemetry.opamp.gateway.domain.server.TlsConnectionSettingsDomain;

public record OwnTelemetryConnectionSetting(
        Integer id,
        String destinationEndpoint,
        HeadersDomain headers,
        TlsCertificateDomain certificate,
        TlsConnectionSettingsDomain connection,
        ProxyConnectionSettingsDomain proxy
) {
}