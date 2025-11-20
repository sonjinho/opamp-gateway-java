package io.opentelemetry.opamp.owntelemetry.adapter.outbound.persistence;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.opentelemetry.opamp.owntelemetry.adapter.outbound.persistence.entity.OwnTelemetrySettingEntity;
import io.opentelemetry.opamp.owntelemetry.adapter.outbound.persistence.repository.OwnTelemetrySettingEntityRepository;
import io.opentelemetry.opamp.owntelemetry.application.port.LoadOwnTelemetrySettingPort;
import io.opentelemetry.opamp.owntelemetry.application.port.SearchOwnTelemetryQuery;
import io.opentelemetry.opamp.owntelemetry.application.port.UpdateOwnTelemetrySettingPort;
import io.opentelemetry.opamp.owntelemetry.domain.OwnTelemetrySetting;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static io.opentelemetry.opamp.owntelemetry.adapter.outbound.persistence.entity.QOwnTelemetrySettingEntity.ownTelemetrySettingEntity;

@Component
@RequiredArgsConstructor
public class OwnTelemetryPersistenceAdapter implements LoadOwnTelemetrySettingPort, UpdateOwnTelemetrySettingPort {

    private final OwnTelemetrySettingEntityRepository repo;
    private final JPAQueryFactory queryFactory;

    @Transactional(readOnly = true)
    @Override
    public OwnTelemetrySetting loadOwnTelemetry(UUID uuid) {
        final var entity = repo.findById(uuid);
        if (entity.isEmpty()) throw new EntityNotFoundException("OwnTelemetrySetting not found with id: " + uuid);
        return entity.get().toDomain();
    }

    @Transactional(readOnly = true)
    @Override
    public List<OwnTelemetrySetting.OwnTelemetrySettingSummary> searchOwnTelemetry(SearchOwnTelemetryQuery query) {
        // optimize the code
        List<BooleanExpression> expressions = new ArrayList<>(10);
        if (!Objects.isNull(query.type()) && !query.type().isBlank()) {
            expressions.add(ownTelemetrySettingEntity.type.eq(query.type()));
        }
        if (Objects.nonNull(query.ownMetric())) {
            expressions.add(ownTelemetrySettingEntity.ownMetric.eq(query.ownMetric()));
        }
        if (Objects.nonNull(query.ownTrace())) {
            expressions.add(ownTelemetrySettingEntity.ownTrace.eq(query.ownTrace()));
        }
        if (Objects.nonNull(query.ownLog())) {
            expressions.add(ownTelemetrySettingEntity.ownLog.eq(query.ownLog()));
        }
        var entities = expressions.isEmpty()
                ? queryFactory.select(ownTelemetrySettingEntity).fetch()
                : queryFactory.select(ownTelemetrySettingEntity).where(expressions.toArray(new BooleanExpression[0])).fetch();

        if (query.labels().isEmpty()) return entities.stream().map(OwnTelemetrySettingEntity::toDomainItem).toList();
        else {
            return entities.stream()
                    .filter(entity ->
                            query.labels().entrySet().stream()
                                    .allMatch(entry -> entry.getValue().equals(entity.getLabels().get(entry.getKey())))
                    )
                    .map(OwnTelemetrySettingEntity::toDomainItem).toList();
        }

    }

    @Override
    public OwnTelemetrySetting.OwnTelemetrySettingSummary save(OwnTelemetrySetting ownTelemetrySetting) {
        if (Objects.nonNull(ownTelemetrySetting.id()) && repo.existsById(ownTelemetrySetting.id()))
            throw new IllegalArgumentException("OwnTelemetrySetting already exists with id: " + ownTelemetrySetting.id());
        var entity = repo.save(OwnTelemetrySettingEntity.from(ownTelemetrySetting));
        return entity.toDomainItem();
    }

    @Override
    public OwnTelemetrySetting.OwnTelemetrySettingSummary update(OwnTelemetrySetting ownTelemetrySetting) {
        if (Objects.nonNull(ownTelemetrySetting.id()) && !repo.existsById(ownTelemetrySetting.id()))
            throw new EntityNotFoundException("OwnTelemetrySetting not found with id: " + ownTelemetrySetting.id());
        var entity = repo.save(OwnTelemetrySettingEntity.from(ownTelemetrySetting));
        return entity.toDomainItem();
    }

    @Override
    public UUID delete(UUID uuid) {
        return null;
    }
}
