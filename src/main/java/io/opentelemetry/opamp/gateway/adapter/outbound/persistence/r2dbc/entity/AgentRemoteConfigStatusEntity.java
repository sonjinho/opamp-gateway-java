package io.opentelemetry.opamp.gateway.adapter.outbound.persistence.r2dbc.entity;

import io.opentelemetry.opamp.gateway.domain.agent.RemoteConfigStatusDomain;
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
@Table(name = "agent_remote_config_status")
public class AgentRemoteConfigStatusEntity {
    @Id
    private Long id;
    @Column(value = "hash")
    private String hash;
    @Column()
    private byte[] lastRemoteConfigHash;
    @Column(value = "status")
    private Byte status;
    @Column(value = "error_message")
    private String errorMessage;

    public RemoteConfigStatusDomain toDomain() {
        return new RemoteConfigStatusDomain(
                lastRemoteConfigHash,
                Byte.toUnsignedInt(status),
                errorMessage
        );

    }

    public void merge(AgentRemoteConfigStatusEntity target) {
        if (target.hash != null) {
            this.hash = target.hash;
        }
        if (target.lastRemoteConfigHash != null) {
            this.lastRemoteConfigHash = target.lastRemoteConfigHash;
        }
        if (target.status != null) {
            this.status = target.status;
        }
        if (target.errorMessage != null) {
            this.errorMessage = target.errorMessage;
        }
    }
}
