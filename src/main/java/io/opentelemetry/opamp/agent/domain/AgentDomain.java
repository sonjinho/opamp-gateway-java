package io.opentelemetry.opamp.agent.domain;

import io.opentelemetry.opamp.gateway.domain.agent.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class AgentDomain {
    private final UUID instanceUId;
    private final Long capabilities;
    private final AgentDescriptionDomain description;
    private final AgentComponentHealthDomain componentHealth;
    private final EffectiveConfigDomain effectiveConfig;
    private final RemoteConfigStatusDomain remoteConfigStatus;
    private final PackageStatusesDomain packageStatuses;

    private final LocalDateTime disconnectedAt;
}
