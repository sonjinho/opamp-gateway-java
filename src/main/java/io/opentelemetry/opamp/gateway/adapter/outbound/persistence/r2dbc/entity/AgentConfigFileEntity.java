package io.opentelemetry.opamp.gateway.adapter.outbound.persistence.r2dbc.entity;

import io.opentelemetry.opamp.gateway.domain.agent.AgentConfigFileDomain;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// name, effective_config_id

// unique for  effect_config_id  and name
@Table(name = "agent_config_file")
public class AgentConfigFileEntity {
    @Id
    private Long id;
    @Setter
    @ToString.Exclude
    @Column(value = "name")
    private String name;
    @ToString.Exclude
    @Setter

    @Column(value = "body")
    private byte[] body;
    @Column
    private String contentType;

    public AgentConfigFileDomain toDomain() {
        return new AgentConfigFileDomain(body, contentType);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        // effective_config_id, name
        if (this.getClass() != o.getClass()) return false;
        AgentConfigFileEntity that = (AgentConfigFileEntity) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public final int hashCode() {
        // effective_config_id,name
        return Objects.hash(getName());
    }
}
