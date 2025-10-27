package io.opentelemetry.opamp.agent.adapter.outbound.persistence.entity;

import io.opentelemetry.opamp.agent.domain.AgentDomain;
import io.opentelemetry.opamp.gateway.domain.agent.*;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "agent", indexes = {
        @Index(name = "idx_instance_uid", columnList = "instance_uid")
})
public class AgentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @Column(name = "instance_uid")
    private UUID instanceUId;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "agent_description")
    private AgentDescriptionDomain description;
    @Column(name = "capabilities")
    private Long capabilities;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "agent_component_health")
    private AgentComponentHealthDomain componentHealth;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "effective_config")
    private EffectiveConfigDomain effectiveConfig;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "remote_config_status")
    private RemoteConfigStatusDomain remoteConfigStatus;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "package_statuses")
    private PackageStatusesDomain packageStatuses;
    @CreationTimestamp()
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp()
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    @Column(name = "disconnected_at", nullable = true)
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
