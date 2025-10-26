package io.opentelemetry.opamp.gateway.adapter.outbound.persistence.jpa.entity;

import io.opentelemetry.opamp.gateway.domain.agent.AgentDisconnectDomain;
import io.opentelemetry.opamp.gateway.domain.agent.AgentToServerDomain;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "agent_to_server", indexes = {
        @Index(name = "idx_instance_uid", columnList = "instance_uid")
})
public class AgentToServerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "instance_uid")
    private UUID instanceUId;
    @Column(name = "seq_num")
    private Long seqNum;
    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "description_id")
    private AgentDescriptionEntity description;
    @Column(name = "capabilities")
    private Long capabilities;
    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "component_health_id")
    private AgentComponentHealthEntity componentHealth;
    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "effective_config_id")
    private AgentEffectiveConfigEntity effectiveConfig;
    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "remote_config_status_id")
    private AgentRemoteConfigStatusEntity remoteConfigStatus;
    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "package_statuses_id")
    private AgentPackageStatusesEntity packageStatuses;
    @Column
    private Long flags;
    @CreationTimestamp()
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "disconnected_at", nullable = true)
    private LocalDateTime disconnectedAt;

    public static AgentToServerEntity fromDomain(AgentToServerDomain agentToServer) {
        AgentDescriptionEntity descriptionEntity = new AgentDescriptionEntity(
                null,
                agentToServer.description().identifyingAttributes(),
                agentToServer.description().nonIdentifyingAttributes()
        );

        AgentComponentHealthEntity componentHealthEntity = new AgentComponentHealthEntity(
                null,
                "root",
                agentToServer.componentHealth().healthy(),
                agentToServer.componentHealth().startTimeUnixNano(),
                agentToServer.componentHealth().lastError(),
                agentToServer.componentHealth().status(),
                null,
                new HashSet<>()
        );

        var children = agentToServer.componentHealth().componentHealthMap().entrySet().stream()
                .map(entry -> new AgentComponentHealthEntity(
                        null,
                        entry.getKey(),
                        entry.getValue().healthy(),
                        entry.getValue().startTimeUnixNano(),
                        entry.getValue().lastError(),
                        entry.getValue().status(),
                        componentHealthEntity,
                        null)
                )
                .collect(java.util.stream.Collectors.toSet());
        componentHealthEntity.getChildren().addAll(children);

        AgentEffectiveConfigEntity effectiveConfigEntity = new AgentEffectiveConfigEntity(
                null,
                new HashSet<>()
        );
        var agentConfigFiles = agentToServer.effectiveConfig().configMap().entrySet().stream()
                .map(entry -> new AgentConfigFileEntity(
                        null,
                        effectiveConfigEntity,
                        entry.getKey(),
                        entry.getValue().body(),
                        entry.getValue().contentType()
                ))
                .collect(java.util.stream.Collectors.toSet());
        effectiveConfigEntity.getAgentConfigFiles().addAll(agentConfigFiles);

        AgentRemoteConfigStatusEntity remoteConfigStatusEntity = new AgentRemoteConfigStatusEntity(
                null,
                null,
                agentToServer.remoteConfigStatus().lastRemoteConfigHash(),
                agentToServer.remoteConfigStatus().status().byteValue(),
                agentToServer.remoteConfigStatus().errorMessage()
        );

        AgentPackageStatusesEntity packageStatusesEntity = new AgentPackageStatusesEntity(
                null,
                agentToServer.packagesStatuses().serverProvidedAllPackagesHash(),
                agentToServer.packagesStatuses().errorMessage(),
                new HashSet<>()
        );

        var packageStatusesSet = agentToServer.packagesStatuses().packages().entrySet().stream()
                .map(entry -> new AgentPackageStatusEntity(
                        null,
                        packageStatusesEntity,
                        entry.getKey(),
                        entry.getValue().agentHasVersion(),
                        entry.getValue().agentHasHash(),
                        entry.getValue().serverOfferedVersion(),
                        entry.getValue().serverOfferedHash(),
                        entry.getValue().status(),
                        entry.getValue().errorMessage()
                ))
                .collect(java.util.stream.Collectors.toSet());
        packageStatusesEntity.getPackageStatuses().addAll(packageStatusesSet);

        return new AgentToServerEntity(
                null,
                agentToServer.instanceId(),
                agentToServer.seqNum(),
                descriptionEntity,
                agentToServer.capabilities(),
                componentHealthEntity,
                effectiveConfigEntity,
                remoteConfigStatusEntity,
                packageStatusesEntity,
                agentToServer.flags(),
                agentToServer.createdAt(),
                agentToServer.disconnectedAt()
        );
    }

    public AgentToServerDomain toDomain() {
        return new AgentToServerDomain(
                instanceUId,
                seqNum,
                description.toDomain(),
                capabilities,
                componentHealth.toDomain(),
                effectiveConfig.toDomain(),
                remoteConfigStatus.toDomain(),
                packageStatuses.toDomain(),
                new AgentDisconnectDomain(),
                flags,
                createdAt,
                disconnectedAt
        );
    }


}
