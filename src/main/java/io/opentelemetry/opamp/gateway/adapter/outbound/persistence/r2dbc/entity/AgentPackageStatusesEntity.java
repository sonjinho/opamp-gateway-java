package io.opentelemetry.opamp.gateway.adapter.outbound.persistence.r2dbc.entity;

import io.opentelemetry.opamp.gateway.domain.agent.PackageStatusesDomain;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Table(value = "agent_package_statuses")
public class AgentPackageStatusesEntity {
    @Id
    private Long id;
    @Column(value = "server_provided_all_packages_hash")
    private byte[] serverProvidedAllPackagesHash;
    @Column(value = "error_message")
    private String errorMessage;

    public PackageStatusesDomain toDomain() {
        return new PackageStatusesDomain(
                null,
                serverProvidedAllPackagesHash,
                errorMessage
        );
    }

    public void merge(AgentPackageStatusesEntity target) {
        if (target.serverProvidedAllPackagesHash != null) {
            this.serverProvidedAllPackagesHash = target.serverProvidedAllPackagesHash;
        }
        if (target.errorMessage != null) {
            this.errorMessage = target.errorMessage;
        }
    }
}
