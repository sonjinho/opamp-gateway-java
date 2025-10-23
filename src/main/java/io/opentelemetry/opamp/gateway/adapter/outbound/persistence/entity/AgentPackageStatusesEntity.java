package io.opentelemetry.opamp.gateway.adapter.outbound.persistence.entity;

import io.opentelemetry.opamp.gateway.domain.agent.PackageStatusesDomain;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "agent_package_statuses")
public class AgentPackageStatusesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "agent_id")
    private Long agentId;
    @Column(name = "server_provided_all_packages_hash")
    private byte[] serverProvidedAllPackagesHash;
    @Column(name = "error_message")
    private String errorMessage;
    @OneToMany(mappedBy = "packageStatuses", orphanRemoval = true)
    private Set<AgentPackageStatusEntity> packageStatuses;

    public PackageStatusesDomain toDomain() {
        return new PackageStatusesDomain(
                packageStatuses.stream().collect(Collectors.toMap(AgentPackageStatusEntity::getName, AgentPackageStatusEntity::toDomain)),
                serverProvidedAllPackagesHash,
                errorMessage
        );
    }
}
