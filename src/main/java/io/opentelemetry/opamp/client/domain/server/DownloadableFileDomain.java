package io.opentelemetry.opamp.client.domain.server;

public record DownloadableFileDomain(
    String downloadUrl,
    byte[] contentHash,
    byte[] signature,
    HeadersDomain headers
) {
}
