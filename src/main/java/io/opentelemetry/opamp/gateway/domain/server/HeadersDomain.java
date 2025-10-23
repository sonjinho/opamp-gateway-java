package io.opentelemetry.opamp.gateway.domain.server;

import java.util.List;

public record HeadersDomain(
    List<HeaderDomain> headers
) {
}