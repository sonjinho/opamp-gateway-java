package io.opentelemetry.opamp.gateway.adapter.outbound.persistence.jpa.entity;

import io.opentelemetry.opamp.gateway.domain.agent.AgentConfigFileDomain;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// name, effective_config_id
@Entity
// unique for  effect_config_id  and name
@Table(name = "agent_config_file", indexes = {
        @Index(name = "idx_effective_config_id_name", columnList = "effective_config_id,name", unique = true)
})
public class AgentConfigFileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @Setter
    @ToString.Exclude
    @ManyToOne()
    @JoinColumn(name = "effective_config_id")
    private AgentEffectiveConfigEntity effectiveConfig;
    @Column
    private String name;
    @ToString.Exclude
    @Setter
    @Lob()
    @Column(length = 65536)
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
        if (this instanceof HibernateProxy) {
            o = ((HibernateProxy) o).getHibernateLazyInitializer().getImplementation();
        }
        if (this.getClass() != o.getClass()) return false;
        AgentConfigFileEntity that = (AgentConfigFileEntity) o;
        return Objects.equals(getName(), that.getName())
                && Objects.equals(getEffectiveConfig().getId(), that.getEffectiveConfig().getId());
    }

    @Override
    public final int hashCode() {
        // effective_config_id,name
        return Objects.hash(getName(), getEffectiveConfig().getId());
    }
}
