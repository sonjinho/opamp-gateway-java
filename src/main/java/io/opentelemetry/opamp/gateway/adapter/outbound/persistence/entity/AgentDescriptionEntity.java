package io.opentelemetry.opamp.gateway.adapter.outbound.persistence.entity;

import io.opentelemetry.opamp.gateway.domain.agent.AgentDescriptionDomain;
import io.opentelemetry.opamp.common.AnyValueMapConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import opamp.proto.Anyvalue;

import java.util.Map;

@Getter
@AllArgsConstructor()
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "agent_description")
public class AgentDescriptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    @Convert(converter = AnyValueMapConverter.class)
    @Column(length = 10000, columnDefinition = "json")
    private Map<String, Anyvalue.AnyValue> identifyingAttributes;

    @Convert(converter = AnyValueMapConverter.class)
    @Column(length = 10000, columnDefinition = "json")
    private Map<String, Anyvalue.AnyValue> nonIdentifyingAttributes;

    public AgentDescriptionDomain toDomain() {
        return new AgentDescriptionDomain(this.identifyingAttributes, this.nonIdentifyingAttributes);
    }
}
