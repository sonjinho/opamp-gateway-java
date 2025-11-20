package io.opentelemetry.opamp.client.domain.server;

import java.util.List;

public record CustomCapabilitiesDomain(
        List<String> capabilities
) {
}
