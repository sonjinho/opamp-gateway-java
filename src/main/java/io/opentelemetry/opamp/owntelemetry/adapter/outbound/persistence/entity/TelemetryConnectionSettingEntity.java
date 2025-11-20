package io.opentelemetry.opamp.owntelemetry.adapter.outbound.persistence.entity;

import io.opentelemetry.opamp.client.domain.server.HeadersDomain;
import io.opentelemetry.opamp.client.domain.server.ProxyConnectionSettingsDomain;
import io.opentelemetry.opamp.client.domain.server.TlsCertificateDomain;
import io.opentelemetry.opamp.client.domain.server.TlsConnectionSettingsDomain;
import io.opentelemetry.opamp.owntelemetry.domain.OwnTelemetryConnectionSetting;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "telemetry_connection_setting")
public class TelemetryConnectionSettingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "headers")
    private Map<String, String> headers;
    @Column(name = "destination_endpoint")
    private String destinationEndpoint;
    @Column(name = "has_tls_certificate")
    Boolean hasTlsCertificate = false;
    @Column(name = "cert")
    private byte[] cert;
    @Column(name = "private_key")
    private byte[] privateKey;
    @Column(name = "ca_cert")
    private byte[] caCert;
    @Column(name = "has_connections_setting")
    private Boolean hasConnectionsSetting = false;
    @Column(name = "ca_pem_contents")
    private String caPemContents;
    @Column(name = "includes_system_ca_concerts_pool")
    private Boolean includesSystemCaConcertsPool;
    @Column(name = "insecure_skip_verify")
    private Boolean inSecureSkipVerify;
    @Column(name = "min_version")
    private String minVersion;
    @Column(name = "max_version")
    private String maxVersion;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "cipher_suites")
    private List<String> cipherSuites;
    @Column(name = "has_proxy_connection_setting")
    private Boolean hasProxyConnectionSetting = false;
    @Column(name = "proxy_url")
    private String url;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "connection_headers")
    private Map<String, String> connectionHeaders;

    public static TelemetryConnectionSettingEntity from(OwnTelemetryConnectionSetting ownTelemetryConnectionSetting) {
        return new TelemetryConnectionSettingEntity(
                null,
                ownTelemetryConnectionSetting.headers() != null ? ownTelemetryConnectionSetting.headers().toMap() : null,
                ownTelemetryConnectionSetting.destinationEndpoint(),
                ownTelemetryConnectionSetting.certificate() != null,
                ownTelemetryConnectionSetting.certificate() != null ? ownTelemetryConnectionSetting.certificate().cert() : null,
                ownTelemetryConnectionSetting.certificate() != null ? ownTelemetryConnectionSetting.certificate().privateKey() : null,
                ownTelemetryConnectionSetting.certificate() != null ? ownTelemetryConnectionSetting.certificate().caCert() : null,
                ownTelemetryConnectionSetting.tls() != null,
                ownTelemetryConnectionSetting.tls() != null ? ownTelemetryConnectionSetting.tls().caPemContents() : null,
                ownTelemetryConnectionSetting.tls() != null ? ownTelemetryConnectionSetting.tls().includeSystemCaCertsPool() : null,
                ownTelemetryConnectionSetting.tls() != null ? ownTelemetryConnectionSetting.tls().insecureSkipVerify() : null,
                ownTelemetryConnectionSetting.tls() != null ? ownTelemetryConnectionSetting.tls().minVersion() : null,
                ownTelemetryConnectionSetting.tls() != null ? ownTelemetryConnectionSetting.tls().maxVersion() : null,
                ownTelemetryConnectionSetting.tls() != null ? ownTelemetryConnectionSetting.tls().cipherSuites() : null,
                ownTelemetryConnectionSetting.proxy() != null,
                ownTelemetryConnectionSetting.proxy() != null ? ownTelemetryConnectionSetting.proxy().url() : null,
                ownTelemetryConnectionSetting.proxy() != null ? ownTelemetryConnectionSetting.proxy().connectHeaders().toMap() : null

        )
                ;
    }

    public OwnTelemetryConnectionSetting toDomain() {
        var header = HeadersDomain.from(headers);
        return new OwnTelemetryConnectionSetting(
                id,
                destinationEndpoint,
                header,
                hasTlsCertificate ? new TlsCertificateDomain(cert, privateKey, caCert) : null,
                hasConnectionsSetting ? new TlsConnectionSettingsDomain(caPemContents, includesSystemCaConcertsPool, inSecureSkipVerify, minVersion, maxVersion, cipherSuites) : null,
                hasProxyConnectionSetting ? new ProxyConnectionSettingsDomain(url, HeadersDomain.from(connectionHeaders)) : null
        );
    }
}
