package io.opentelemetry.opamp.client.domain.server;

import java.util.List;

public record TlsConnectionSettingsDomain(
        String caPemContents,
        Boolean includeSystemCaCertsPool,
        Boolean insecureSkipVerify,
        String minVersion,
        String maxVersion,
        List<String> cipherSuites
) {
}
