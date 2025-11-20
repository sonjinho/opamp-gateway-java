package io.opentelemetry.opamp.client.domain.server;

public record PackageAvailableDomain(
    PackageTypeDomain type,
    String version,
    DownloadableFileDomain file,
    byte[] hash
) {
}