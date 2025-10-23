package io.opentelemetry.opamp.gateway.domain.agent;

import java.util.Map;

public record PackageStatusesDomain(
        Map<String, PackageStatusDomain> packages,
        byte[] serverProvidedAllPackagesHash,
        String errorMessage
) {

}

