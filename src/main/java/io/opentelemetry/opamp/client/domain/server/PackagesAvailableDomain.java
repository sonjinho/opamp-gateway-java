package io.opentelemetry.opamp.client.domain.server;

import java.util.Map;

public record PackagesAvailableDomain(
    Map<String, PackageAvailableDomain> packages,
    byte[] allPackagesHash
) {
}
