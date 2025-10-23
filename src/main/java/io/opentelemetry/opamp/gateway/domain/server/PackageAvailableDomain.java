package io.opentelemetry.opamp.gateway.domain.server;

public record PackageAvailableDomain(
    PackageTypeDomain type,
    String version,
    DownloadableFileDomain file,
    byte[] hash
) {
}