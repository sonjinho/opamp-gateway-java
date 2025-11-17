package io.opentelemetry.opamp.receiver.adapter.outbound.persistence.entity;

import io.opentelemetry.opamp.receiver.domain.OwnTelemetrySetting;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "own_telemetry_setting", indexes = {
        @Index(name = "idx_type", columnList = "deploy_type"),
        @Index(name = "idx_own_metric", columnList = "own_metric"),
        @Index(name = "idx_own_trace", columnList = "own_trace"),
        @Index(name = "idx_own_log", columnList = "own_log")
}
)
public class OwnTelemetrySettingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;
    @Column(name = "deploy_type")
    private String type;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "labels")
    private Map<String, String> labels;
    @Column(name = "own_metric")
    private Boolean ownMetric;
    @Column(name = "own_trace")
    private Boolean ownTrace;
    @Column(name = "own_log")
    private Boolean ownLog;
    @ManyToOne
    @JoinColumn(name = "own_metric_setting_id")
    private TelemetryConnectionSettingEntity ownMetricSetting;
    @ManyToOne
    @JoinColumn(name = "own_trace_setting_id")
    private TelemetryConnectionSettingEntity ownTraceSetting;
    @ManyToOne()
    @JoinColumn(name = "own_log_setting_id")
    private TelemetryConnectionSettingEntity ownLogSetting;

    public static OwnTelemetrySettingEntity from(OwnTelemetrySetting ownTelemetrySetting) {
        return new OwnTelemetrySettingEntity(
                ownTelemetrySetting.id(),
                ownTelemetrySetting.type(),
                ownTelemetrySetting.labels(),
                ownTelemetrySetting.ownMetricEnable(),
                ownTelemetrySetting.ownTraceEnable(),
                ownTelemetrySetting.ownLogEnable(),
                ownTelemetrySetting.ownMetricEnable() ? TelemetryConnectionSettingEntity.from(ownTelemetrySetting.ownMetricSetting()) : null,
                ownTelemetrySetting.ownTraceEnable() ? TelemetryConnectionSettingEntity.from(ownTelemetrySetting.ownTraceSetting()) : null,
                ownTelemetrySetting.ownLogEnable() ? TelemetryConnectionSettingEntity.from(ownTelemetrySetting.ownLogSetting()) : null
        );
    }

    public OwnTelemetrySetting.OwnTelemetrySettingSummary toDomainItem() {
        return new OwnTelemetrySetting.OwnTelemetrySettingSummary(
                id,
                type,
                labels,
                ownMetric,
                ownTrace,
                ownLog
        );
    }

    public OwnTelemetrySetting toDomain() {
        return new OwnTelemetrySetting(
                id,
                type,
                labels,
                ownMetric,
                ownTrace,
                ownLog,
                ownMetric ? ownMetricSetting.toDomain() : null,
                ownTrace ? ownTraceSetting.toDomain() : null,
                ownLog ? ownLogSetting.toDomain() : null
        );
    }
}
