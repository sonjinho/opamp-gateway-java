package io.opentelemetry.opamp.telemetry.log.application.port;

public record LogSearchQuery(
        String query,
        Long limit,
        String time,
        String direction
) {
    // Validate if query is null or empty throw exception
    public LogSearchQuery {
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("Query cannot be null or empty");
        }
    }
}
