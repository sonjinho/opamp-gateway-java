package io.opentelemetry.opamp.gateway.domain.agent;

import java.time.LocalDateTime;
import java.util.UUID;

public record AgentToServerDomain(
        UUID instanceId,
        Long seqNum,
        AgentDescriptionDomain description,
        Long capabilities,
        AgentComponentHealthDomain componentHealth,
        EffectiveConfigDomain effectiveConfig,
        RemoteConfigStatusDomain remoteConfigStatus,
        PackageStatusesDomain packagesStatus,
        AgentDisconnectDomain agentDisconnect,
        Long flags,
        LocalDateTime createdAt,
        LocalDateTime disconnectedAt
) {

    public AgentToServerDomain(
            UUID instanceId,
            Long seqNum,
            AgentDescriptionDomain description,
            Long capabilities,
            AgentComponentHealthDomain componentHealth,
            EffectiveConfigDomain effectiveConfig,
            RemoteConfigStatusDomain remoteConfigStatus,
            PackageStatusesDomain packagesStatus,
            AgentDisconnectDomain agentDisconnect,
            Long flags
    ) {
        this(
                instanceId,
                seqNum,
                description,
                capabilities,
                componentHealth,
                effectiveConfig,
                remoteConfigStatus,
                packagesStatus,
                agentDisconnect,
                flags,
                null,
                null
        );
    }


    public boolean hasAgentDisconnect() {
        return agentDisconnect != null;
    }

    public boolean hasEffectiveConfig() {
        return effectiveConfig != null;
    }

    public boolean hasRemoteConfigStatus() {
        return remoteConfigStatus != null;
    }

    public boolean hasPackagesStatus() {
        return packagesStatus != null;
    }
}

