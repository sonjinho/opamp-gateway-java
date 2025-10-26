package io.opentelemetry.opamp.gateway.adapter.outbound.persistence.jpa.entity;

import io.opentelemetry.opamp.gateway.domain.agent.RemoteConfigStatusDomain;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "agent_remote_config_status")
public class AgentRemoteConfigStatusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "hash")
    private String hash;
    @Column()
    private byte[] lastRemoteConfigHash;
    @Column(name = "status")
    private Byte status;
    @Column(name = "error_message")
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
