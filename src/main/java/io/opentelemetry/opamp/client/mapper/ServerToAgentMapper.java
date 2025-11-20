package io.opentelemetry.opamp.client.mapper;

import com.google.protobuf.ByteString;
import io.opentelemetry.opamp.client.domain.server.*;
import io.opentelemetry.opamp.common.util.UUIDUtil;
import opamp.proto.Opamp;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static io.opentelemetry.opamp.client.mapper.AgentCapabilitiesHandler.*;

@Component
public class ServerToAgentMapper {

    public Opamp.ServerToAgent mapperToProto(ServerToAgentDomain serverToAgentDomain) {
        var builder = Opamp.ServerToAgent.newBuilder()
                .setInstanceUid(ByteString.copyFrom(UUIDUtil.INSTANCE.convertUUIDToBytes(serverToAgentDomain.instanceId())))
                .setCapabilities(
                        AgentCapabilitiesHandler.addCapability(List.of(REPORTS_STATUS, ACCEPTS_REMOTE_CONFIG, REPORTS_EFFECTIVE_CONFIG, REPORTS_HEALTH, REPORTS_REMOTE_CONFIG, REPORTS_HEARTBEAT, REPORTS_OWN_LOGS, REPORTS_OWN_METRICS, REPORTS_OWN_TRACES))
                )
                .setFlags(serverToAgentDomain.flags());
        if (Objects.nonNull(serverToAgentDomain.agentIdentification())) {
            builder.setAgentIdentification(Opamp.AgentIdentification.newBuilder()
                    .setNewInstanceUid(
                            ByteString.copyFrom(
                                    UUIDUtil.INSTANCE.convertUUIDToBytes(
                                            serverToAgentDomain.agentIdentification().newInstanceUid()
                                    )
                            )
                    ));
        }
        if (Objects.nonNull(serverToAgentDomain.agentRemoteConfig()))
            builder.setRemoteConfig(mapperToProtoAgentRemoteConfig(serverToAgentDomain.agentRemoteConfig()));
        if (Objects.nonNull(serverToAgentDomain.connectionSettingsOffers())) {
            builder.setConnectionSettings(mapperToProtoConnectionSettingsOffers(serverToAgentDomain.connectionSettingsOffers()));
        }
        if (Objects.nonNull(serverToAgentDomain.command())) {
            if (serverToAgentDomain.command().commandType().equals(CommandType.RESTART))
                builder.setCommand(Opamp.ServerToAgentCommand.newBuilder().setType(Opamp.CommandType.CommandType_Restart).build());
        }
        return builder.build();
    }

    public Opamp.AgentRemoteConfig mapperToProtoAgentRemoteConfig(AgentRemoteConfigDomain agentRemoteConfig) {
        Map<String, Opamp.AgentConfigFile> configMap = agentRemoteConfig.config().configMap()
                .entrySet().stream()
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> Opamp.AgentConfigFile
                                        .newBuilder()
                                        .setContentType(entry.getValue().contentType())
                                        .setBody(ByteString.copyFrom(entry.getValue().body()))
                                        .build()
                        )
                );

        return Opamp.AgentRemoteConfig.newBuilder()
                .setConfig(Opamp.AgentConfigMap.newBuilder().putAllConfigMap(configMap).build())
                .setConfigHash(ByteString.copyFrom(agentRemoteConfig.configHash()))
                .build();
    }

    private Opamp.ConnectionSettingsOffers mapperToProtoConnectionSettingsOffers(ConnectionSettingsOffersDomain connectionSettingsOffersDomain) {
        var builder = Opamp.ConnectionSettingsOffers.newBuilder()
                .setHash(ByteString.copyFrom(connectionSettingsOffersDomain.hash()));
        if (Objects.nonNull(connectionSettingsOffersDomain.opamp())) {
            // TODO: OPAMP Setting
        }

        if (Objects.nonNull(connectionSettingsOffersDomain.ownMetrics())) {
            builder.setOwnMetrics(mapperToProtoTelemetryConnectionSettings(connectionSettingsOffersDomain.ownMetrics()));
        }

        if (Objects.nonNull(connectionSettingsOffersDomain.ownLogs())) {
            builder.setOwnLogs(mapperToProtoTelemetryConnectionSettings(connectionSettingsOffersDomain.ownLogs()));
        }
        if (Objects.nonNull(connectionSettingsOffersDomain.ownTraces())) {
            builder.setOwnTraces(mapperToProtoTelemetryConnectionSettings(connectionSettingsOffersDomain.ownTraces()));
        }

        return builder.build();
    }

    private Opamp.TelemetryConnectionSettings mapperToProtoTelemetryConnectionSettings(TelemetryConnectionSettingsDomain domain) {
        var builder = Opamp.TelemetryConnectionSettings.newBuilder()
                .setDestinationEndpoint(domain.destinationEndpoint());
        if (Objects.nonNull(domain.headers())) {
            builder.setHeaders(
                    Opamp.Headers.newBuilder()
                            .addAllHeaders(
                                    domain.headers().headers()
                                            .stream().map(header ->
                                                    Opamp.Header
                                                            .newBuilder()
                                                            .setKey(header.key())
                                                            .setValue(header.value()).build()
                                            ).toList()
                            )
            );
        }
        if (Objects.nonNull(domain.certificate())) {
            // converter
            builder.setCertificate(
                    Opamp.TLSCertificate.newBuilder()
                            .setCert(ByteString.copyFrom(domain.certificate().cert()))
                            .setPrivateKey(ByteString.copyFrom(domain.certificate().privateKey()))
                            .setCaCert(ByteString.copyFrom(domain.certificate().caCert()))
                            .build()

            );
        }
        if (Objects.nonNull(domain.tls())) {
            builder.setTls(
                    Opamp.TLSConnectionSettings.newBuilder()
                            .setCaPemContents(domain.tls().caPemContents())
                            .setIncludeSystemCaCertsPool(domain.tls().includeSystemCaCertsPool())
                            .setInsecureSkipVerify(domain.tls().insecureSkipVerify())
                            .setMinVersion(domain.tls().minVersion())
                            .setMaxVersion(domain.tls().maxVersion())
                            .addAllCipherSuites(domain.tls().cipherSuites())
                            .build()
            );
        }
        if (Objects.nonNull(domain.proxy())) {
            builder.setProxy(
                    Opamp.ProxyConnectionSettings.newBuilder()
                            .setUrl(domain.proxy().url())
                            .setConnectHeaders(
                                    Opamp.Headers.newBuilder()
                                            .addAllHeaders(
                                                    domain.proxy().connectHeaders().headers()
                                                            .stream().map(header ->
                                                                    Opamp.Header
                                                                            .newBuilder()
                                                                            .setKey(header.key())
                                                                            .setValue(header.value()).build()
                                                            ).toList()
                                            )
                            )
                            .build()
            );
        }

        return builder.build();
    }
}
