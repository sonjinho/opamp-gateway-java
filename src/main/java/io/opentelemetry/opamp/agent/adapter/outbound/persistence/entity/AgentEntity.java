package io.opentelemetry.opamp.agent.adapter.outbound.persistence.entity;

import io.opentelemetry.opamp.agent.domain.AgentDomain;
import io.opentelemetry.opamp.gateway.domain.agent.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "agent")
public class AgentEntity {
    @Id
    private Long id;
    @Column(value = "instance_uid")
    private UUID instanceUId;
    @Column(value = "agent_description")
    private AgentDescriptionDomain description;
    @Column(value = "capabilities")
    private Long capabilities;
    @Column(value = "agent_component_health")
    private AgentComponentHealthDomain componentHealth;
    @Column(value = "effective_config")
    private EffectiveConfigDomain effectiveConfig;
    @Column(value = "remote_config_status")
    private RemoteConfigStatusDomain remoteConfigStatus;
    @Column(value = "package_statuses")
    private PackageStatusesDomain packageStatuses;
    @CreatedDate()
    @Column(value = "created_at")
    private LocalDateTime createdAt;
    @LastModifiedDate()
    @Column(value = "updated_at")
    private LocalDateTime updatedAt;
    @Column(value = "disconnected_at")
    private LocalDateTime disconnectedAt;

    public static AgentEntity from(AgentDomain agent) {
        return new AgentEntity(
                null,
                agent.instanceUId(),
                agent.description(),
                agent.capabilities(),
                agent.componentHealth(),
                agent.effectiveConfig(),
                agent.remoteConfigStatus(),
                agent.packagesStatuses(),
                agent.createdAt(),
                null,
                agent.disconnectedAt()
        );
    }

    public void merge(AgentEntity target) {
        // validation
        if (!this.instanceUId.equals(target.instanceUId)) {
            return;
        }
        // merge current AgentEntity
        // if not exist current, replace target
        // if exist, add target to current
        // if exist, but empty replace target
        // if both exist update to target
        if (target.description != null) {
            if (this.description == null) {
                this.description = target.description;
            } else {
                this.description.merge(target.description);
            }
        }

        if (target.capabilities != null) {
            this.capabilities = target.capabilities;
        }

        if (target.componentHealth != null) {
            if (this.componentHealth == null) {
                this.componentHealth = target.componentHealth;
            } else {
                this.componentHealth = AgentComponentHealthDomain.merge(this.componentHealth, target.componentHealth);
            }
        }

        if (target.effectiveConfig != null) {
            if (this.effectiveConfig == null) {
                this.effectiveConfig = target.effectiveConfig;
            } else {
                this.effectiveConfig = EffectiveConfigDomain.merge(this.effectiveConfig, target.effectiveConfig);
            }
        }

        if (target.remoteConfigStatus != null) {
            if (this.remoteConfigStatus == null) {
                this.remoteConfigStatus = target.remoteConfigStatus;
            } else {
                this.remoteConfigStatus = RemoteConfigStatusDomain.merge(this.remoteConfigStatus, target.remoteConfigStatus);
            }
        }

        if (target.packageStatuses != null) {
            if (this.packageStatuses == null) {
                this.packageStatuses = target.packageStatuses;
            } else {
                this.packageStatuses = PackageStatusesDomain.merge(this.packageStatuses, target.packageStatuses);
            }
        }

        if (target.disconnectedAt != null) {
            this.disconnectedAt = target.disconnectedAt;
        }
    }

    public AgentDomain toDomain() {
        return new AgentDomain(
                this.instanceUId,
                this.capabilities,
                this.description,
                this.componentHealth,
                this.effectiveConfig,
                this.remoteConfigStatus,
                this.packageStatuses,
                this.createdAt,
                this.updatedAt,
                this.disconnectedAt
        );
    }
}
