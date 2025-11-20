package io.opentelemetry.opamp.client.domain.server;

import io.opentelemetry.opamp.owntelemetry.domain.OwnTelemetryConnectionSetting;

public record TelemetryConnectionSettingsDomain(
    String destinationEndpoint,
    HeadersDomain headers,
    TlsCertificateDomain certificate,
    TlsConnectionSettingsDomain tls,
    ProxyConnectionSettingsDomain proxy
) {
    public static TelemetryConnectionSettingsDomain from(OwnTelemetryConnectionSetting setting) {
        return new TelemetryConnectionSettingsDomain(
                setting.destinationEndpoint(),
                setting.headers(),
                setting.certificate(),
                setting.tls(),
                setting.proxy()
        );
    }
}
