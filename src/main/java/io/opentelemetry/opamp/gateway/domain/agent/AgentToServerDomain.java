package io.opentelemetry.opamp.gateway.domain.agent;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public record AgentToServerDomain(UUID instanceId, Long seqNum, AgentDescriptionDomain description, Long capabilities,
                                  AgentComponentHealthDomain componentHealth, EffectiveConfigDomain effectiveConfig,
                                  RemoteConfigStatusDomain remoteConfigStatus, PackageStatusesDomain packagesStatuses,
                                  AgentDisconnectDomain agentDisconnect, Long flags, LocalDateTime createdAt,
                                  LocalDateTime disconnectedAt) {

    public AgentToServerDomain(UUID instanceId, Long seqNum, AgentDescriptionDomain description, Long capabilities, AgentComponentHealthDomain componentHealth, EffectiveConfigDomain effectiveConfig, RemoteConfigStatusDomain remoteConfigStatus, PackageStatusesDomain packagesStatus, AgentDisconnectDomain agentDisconnect, Long flags) {
        this(instanceId, seqNum, description, capabilities, componentHealth, effectiveConfig, remoteConfigStatus, packagesStatus, agentDisconnect, flags, LocalDateTime.now(), null);
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
        return packagesStatuses != null;
    }

    // skip the sequence
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AgentToServerDomain that = (AgentToServerDomain) o;
        return Objects.equals(flags, that.flags) && Objects.equals(instanceId, that.instanceId) && Objects.equals(capabilities, that.capabilities) && Objects.equals(createdAt, that.createdAt) && Objects.equals(disconnectedAt, that.disconnectedAt) && Objects.equals(description, that.description) && Objects.equals(effectiveConfig, that.effectiveConfig) && Objects.equals(agentDisconnect, that.agentDisconnect) && Objects.equals(packagesStatuses, that.packagesStatuses) && Objects.equals(componentHealth, that.componentHealth) && Objects.equals(remoteConfigStatus, that.remoteConfigStatus);
    }

    // skip the sequence
    @Override
    public int hashCode() {
        return Objects.hash(instanceId, description, capabilities, componentHealth, effectiveConfig, remoteConfigStatus, packagesStatuses, agentDisconnect, flags, createdAt, disconnectedAt);
    }
}

