package io.opentelemetry.opamp.gateway.adapter.outbound.persistence.r2dbc;

import io.opentelemetry.opamp.gateway.adapter.outbound.persistence.r2dbc.entity.AgentToServerEntity;
import io.opentelemetry.opamp.gateway.adapter.outbound.persistence.r2dbc.repository.AgentToServerEntityRepository;
import io.opentelemetry.opamp.gateway.application.port.LoadAgentToServerPort;
import io.opentelemetry.opamp.gateway.application.port.UpdateAgentToServerPort;
import io.opentelemetry.opamp.gateway.domain.agent.AgentToServerDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@ConditionalOnProperty(name = "request.persistence.type", havingValue = "DB")
@Component
@RequiredArgsConstructor
public class AgentToServerR2dbcPersistenceAdapter implements LoadAgentToServerPort, UpdateAgentToServerPort {

    private final AgentToServerEntityRepository repo;

    @Override
    public Mono<AgentToServerDomain> loadAgentToServer(UUID uuid) {
        return repo.findByInstanceUId(uuid)
                .map(AgentToServerEntity::toDomain);
    }

    @Override
    public Mono<Boolean> isExist(UUID uuid) {
        return repo.findByInstanceUId(uuid)
                .map(agentToServerEntity -> true)
                .switchIfEmpty(Mono.just(false));
    }

    @Override
    public Mono<Boolean> saveAgentToServer(AgentToServerDomain agentToServer) {
        var entity = AgentToServerEntity.fromDomain(agentToServer);
        return repo.save(entity)
                .map(result -> true)
                .onErrorReturn(false);
    }
}
