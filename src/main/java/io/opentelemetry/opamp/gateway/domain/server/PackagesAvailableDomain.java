package io.opentelemetry.opamp.gateway.domain.server;

import java.util.Map;

public record PackagesAvailableDomain(
    Map<String, PackageAvailableDomain> packages,
    byte[] allPackagesHash
) {
}
