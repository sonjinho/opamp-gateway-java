package io.opentelemetry.opamp.client.domain.server;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public record HeadersDomain(
        List<HeaderDomain> headers
) {
    public static HeadersDomain from(Map<String, String> headers) {
        if (Objects.isNull(headers)) return new HeadersDomain(Collections.emptyList());
        return new HeadersDomain(headers.entrySet().stream().map(entry -> HeaderDomain.from(entry.getKey(), entry.getValue())).toList());
    }

    public Map<String, String> toMap() {
        return headers.stream().collect(Collectors.toMap(HeaderDomain::key, HeaderDomain::value));
    }
}