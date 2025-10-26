package io.opentelemetry.opamp.gateway.adapter.outbound.persistence.jpa.entity;

import io.opentelemetry.opamp.gateway.domain.agent.PackageStatusDomain;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "agent_package_status")
public class AgentPackageStatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "package_statuses_id")
    private AgentPackageStatusesEntity packageStatuses;
    @Column(name = "name")
    private String name;
    @Column(name = "agent_has_version")
    private String agentHasVersion;
    @Column(name = "agent_has_hash")
    private byte[] agentHasHash;
    @Column(name = "server_offered_version")
    private String serverOfferedVersion;
    @Column(name = "server_offered_hash")
    private byte[] serverOfferedHash;
    @Column(name = "status")
    private Integer status;
    @Column(name = "error_message")
    private String errorMessage;

    public PackageStatusDomain toDomain() {
        return new PackageStatusDomain(
                name,
                agentHasVersion,
                agentHasHash,
                serverOfferedVersion,
                serverOfferedHash,
                status,
                errorMessage
        );
    }
}
