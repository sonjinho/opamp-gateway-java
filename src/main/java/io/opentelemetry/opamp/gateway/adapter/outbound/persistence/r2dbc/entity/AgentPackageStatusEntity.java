package io.opentelemetry.opamp.gateway.adapter.outbound.persistence.r2dbc.entity;

import io.opentelemetry.opamp.gateway.domain.agent.PackageStatusDomain;
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

@Table(value = "agent_package_status")
public class AgentPackageStatusEntity {

    @Id
    private Long id;
    @Column(value = "name")
    private String name;
    @Column(value = "agent_has_version")
    private String agentHasVersion;
    @Column(value = "agent_has_hash")
    private byte[] agentHasHash;
    @Column(value = "server_offered_version")
    private String serverOfferedVersion;
    @Column(value = "server_offered_hash")
    private byte[] serverOfferedHash;
    @Column(value = "status")
    private Integer status;
    @Column(value = "error_message")
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
