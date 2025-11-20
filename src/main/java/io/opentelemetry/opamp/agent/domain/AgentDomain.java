package io.opentelemetry.opamp.agent.domain;

import io.opentelemetry.opamp.client.domain.agent.*;

import java.time.LocalDateTime;
import java.util.UUID;

public record AgentDomain(UUID instanceUId, Long capabilities, AgentDescriptionDomain description,
                          AgentComponentHealthDomain componentHealth, EffectiveConfigDomain effectiveConfig,
                          RemoteConfigStatusDomain remoteConfigStatus, PackageStatusesDomain packagesStatuses,
                          LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime disconnectedAt) {


}
