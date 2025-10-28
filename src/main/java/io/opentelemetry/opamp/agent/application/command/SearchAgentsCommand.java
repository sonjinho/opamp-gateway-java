package io.opentelemetry.opamp.agent.application.command;

import lombok.Builder;

@Builder
public record SearchAgentsCommand(
        boolean active
) {
}
