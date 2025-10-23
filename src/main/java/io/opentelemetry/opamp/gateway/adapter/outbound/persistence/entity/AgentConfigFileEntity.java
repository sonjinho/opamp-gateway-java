package io.opentelemetry.opamp.gateway.adapter.outbound.persistence.entity;

import io.opentelemetry.opamp.gateway.domain.agent.AgentConfigFileDomain;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "agent_config_file")
public class AgentConfigFileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "effective_config_id")
    private AgentEffectiveConfigEntity effectiveConfig;

    @Column
    private String name;
    @Lob()
    @Column(length = 65536)
    private byte[] body;
    @Column
    private String contentType;

    public AgentConfigFileDomain toDomain() {
        return new AgentConfigFileDomain(body, contentType);
    }
}
