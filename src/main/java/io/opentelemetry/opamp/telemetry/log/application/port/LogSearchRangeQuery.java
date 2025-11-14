package io.opentelemetry.opamp.telemetry.log.application.port;

public record LogSearchRangeQuery(
        String query,
        Long limit,
        String start,
        String end,
        String since,
        String step,
        String interval,
        String direction
) {
    // Validate if query is null or empty throw exception
    public LogSearchRangeQuery {
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("Query cannot be null or empty");
        }
    }
}
