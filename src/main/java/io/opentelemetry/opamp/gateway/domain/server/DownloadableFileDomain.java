package io.opentelemetry.opamp.gateway.domain.server;

public record DownloadableFileDomain(
    String downloadUrl,
    byte[] contentHash,
    byte[] signature,
    HeadersDomain headers
) {
}
