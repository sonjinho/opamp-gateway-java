package io.opentelemetry.opamp.gateway.domain.server;

import java.util.List;

public record CustomCapabilitiesDomain(
        List<String> capabilities
) {
}
