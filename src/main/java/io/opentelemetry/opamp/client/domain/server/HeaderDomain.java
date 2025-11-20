package io.opentelemetry.opamp.client.domain.server;

public record HeaderDomain(
        String key,
        String value
) {
    public static HeaderDomain from(String key, String value) {
        return new HeaderDomain(key, value);
    }
}
