package io.opentelemetry.opamp.client.mapper;

import io.opentelemetry.opamp.client.domain.agent.*;
import io.opentelemetry.opamp.client.domain.server.CustomCapabilitiesDomain;
import io.opentelemetry.opamp.client.domain.server.CustomMessageDomain;
import lombok.extern.slf4j.Slf4j;
import opamp.proto.Anyvalue;
import opamp.proto.Opamp;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AgentToServerMapper {

    public AgentToServerDomain mapperToDomain(Opamp.AgentToServer request) {
        log.debug("AgentToServer: {}", request);
        return new AgentToServerDomain(
                UUID.nameUUIDFromBytes(request.getInstanceUid().toByteArray()),
                request.getSequenceNum(),
                this.mapperToAgentDescriptionDomain(request.getAgentDescription()),
                request.getCapabilities(),
                this.mapperToAgentComponentHealthDomain(request.getHealth()),
                this.mapperToEffectiveConfigDomain(request.getEffectiveConfig()),
                this.mapperToRemoteConfigStatusDomain(request.getRemoteConfigStatus()),
                this.mapperToPackageStatusesDomain(request.getPackageStatuses()),
                this.mapperToAgentDisconnectDomain(request.getAgentDisconnect()),
                request.getFlags(), this.mapperToConnectionSettingsRequestDomain(request.getConnectionSettingsRequest()),
                new CustomCapabilitiesDomain(request.getCustomCapabilities().getCapabilitiesList()),
                this.mapperToCustomMessageDomain(request.getCustomMessage()),
                this.mapperToAvailableComponentsDomain(request.getAvailableComponents()),
                this.mapperToConnectionSettingsStatusDomain(request.getConnectionSettingsStatus())


        );
    }

    private ConnectionSettingsStatusDomain mapperToConnectionSettingsStatusDomain(Opamp.ConnectionSettingsStatus connectionSettingsStatus) {
        if (Objects.isNull(connectionSettingsStatus)) return null;
        return new ConnectionSettingsStatusDomain(connectionSettingsStatus.getLastConnectionSettingsHash().toByteArray(), connectionSettingsStatus.getStatus().getNumber(), connectionSettingsStatus.getErrorMessage());
    }

    private CustomMessageDomain mapperToCustomMessageDomain(Opamp.CustomMessage customMessage) {
        if (customMessage == null) return null;
        return new CustomMessageDomain(
                customMessage.getCapability(),
                customMessage.getType(),
                customMessage.getData().toByteArray()
        );
    }

    private AvailableComponentsDomain mapperToAvailableComponentsDomain(Opamp.AvailableComponents availableComponents) {
        if (Objects.isNull(availableComponents)) return null;

        Map<String, AvailableComponentsDomain.ComponentDetailsDomain> components = availableComponents.getComponentsCount() == 0 ? Map.of() : availableComponents.getComponentsMap().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> this.mapperToComponentDetailsDomain(entry.getValue())));
        return new AvailableComponentsDomain(availableComponents.getHash().toByteArray(), components);
    }

    private AvailableComponentsDomain.ComponentDetailsDomain mapperToComponentDetailsDomain(Opamp.ComponentDetails componentDetails) {
        Map<String, String> metadata = new HashMap<>();
        for (var kv : componentDetails.getMetadataList()) {
            metadata.put(kv.getKey(), convert(kv.getValue()));
        }
        Map<String, AvailableComponentsDomain.ComponentDetailsDomain> subComponentMap = componentDetails.getSubComponentMapCount() == 0 ? Map.of() : componentDetails.getSubComponentMapMap().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> this.mapperToComponentDetailsDomain(entry.getValue())));

        return new AvailableComponentsDomain.ComponentDetailsDomain(
                metadata,
                subComponentMap
        );
    }

    public AgentDescriptionDomain mapperToAgentDescriptionDomain(Opamp.AgentDescription description) {
        if (description == null) {
            return null;
        }
        Map<String, String> identifyingAttributes = new HashMap<>();
        for (Anyvalue.KeyValue kv : description.getIdentifyingAttributesList()) {
            identifyingAttributes.put(kv.getKey(), convert(kv.getValue()));
        }

        Map<String, String> nonIdentifyingAttributes = new HashMap<>();
        for (Anyvalue.KeyValue kv : description.getNonIdentifyingAttributesList()) {
            nonIdentifyingAttributes.put(kv.getKey(), convert(kv.getValue()));
        }
        return new AgentDescriptionDomain(identifyingAttributes, nonIdentifyingAttributes);
    }

    public AgentComponentHealthDomain mapperToAgentComponentHealthDomain(Opamp.ComponentHealth componentHealth) {
        if (Objects.isNull(componentHealth)) return null;
        Map<String, AgentComponentHealthDomain> componentHealthMap = new HashMap<>();
        for (Map.Entry<String, Opamp.ComponentHealth> entry : componentHealth.getComponentHealthMapMap().entrySet()) {
            componentHealthMap.put(entry.getKey(), this.mapperToAgentComponentHealthDomain(entry.getValue()));
        }
        return new AgentComponentHealthDomain(
                componentHealth.getHealthy(),
                componentHealth.getStartTimeUnixNano(),
                componentHealth.getLastError(),
                componentHealth.getStatus(),
                componentHealth.getStatusTimeUnixNano(),
                componentHealthMap
        );
    }

    public EffectiveConfigDomain mapperToEffectiveConfigDomain(Opamp.EffectiveConfig effectiveConfig) {
        if (effectiveConfig == null) return null;
        Map<String, AgentConfigFileDomain> configMap = new HashMap<>();
        for (Map.Entry<String, Opamp.AgentConfigFile> entry : effectiveConfig.getConfigMap().getConfigMapMap().entrySet()) {
            configMap.put(entry.getKey(), this.mapperToAgentConfigFileDomain(entry.getValue()));
        }
        return new EffectiveConfigDomain(configMap);
    }

    public AgentConfigFileDomain mapperToAgentConfigFileDomain(Opamp.AgentConfigFile value) {
        return new AgentConfigFileDomain(
                value.getBody().toByteArray(),
                value.getContentType()
        );
    }

    public RemoteConfigStatusDomain mapperToRemoteConfigStatusDomain(Opamp.RemoteConfigStatus value) {
        if (value == null) return null;
        return new RemoteConfigStatusDomain(
                value.getLastRemoteConfigHash().toByteArray(),
                value.getStatus().getNumber(),
                value.getErrorMessage()
        );
    }

    private PackageStatusesDomain mapperToPackageStatusesDomain(Opamp.PackageStatuses packageStatuses) {
        if (packageStatuses == null) return null;
        Map<String, PackageStatusDomain> packages = new HashMap<>();
        for (Map.Entry<String, Opamp.PackageStatus> entry : packageStatuses.getPackagesMap().entrySet()) {
            packages.put(entry.getKey(), this.mapperToPackageStatusDomain(entry.getValue()));
        }
        return new PackageStatusesDomain(
                packages,
                packageStatuses.getServerProvidedAllPackagesHash().toByteArray(),
                packageStatuses.getErrorMessage()
        );
    }

    private PackageStatusDomain mapperToPackageStatusDomain(Opamp.PackageStatus value) {
        return new PackageStatusDomain(
                value.getName(),
                value.getAgentHasVersion(),
                value.getAgentHasHash().toByteArray(),
                value.getServerOfferedVersion(),
                value.getServerOfferedHash().toByteArray(),
                value.getStatus().getNumber(),
                value.getErrorMessage()
        );
    }

    private AgentDisconnectDomain mapperToAgentDisconnectDomain(Opamp.AgentDisconnect agentDisconnect) {
        if (agentDisconnect == null) return null;
        return new AgentDisconnectDomain();
    }

    private ConnectionSettingsRequestDomain mapperToConnectionSettingsRequestDomain(Opamp.ConnectionSettingsRequest connectionSettingsRequest) {
        var optional = Optional.ofNullable(connectionSettingsRequest).map(Opamp.ConnectionSettingsRequest::getOpamp).map(Opamp.OpAMPConnectionSettingsRequest::getCertificateRequest).map(Opamp.CertificateRequest::getCsr).orElse(null);
        if (Objects.isNull(optional)) return null;
        return new ConnectionSettingsRequestDomain(
                new ConnectionSettingsRequestDomain.OpampConnectionSettingsRequestDomain(
                        new ConnectionSettingsRequestDomain.OpampConnectionSettingsRequestDomain.CertificateRequestDomain(
                                connectionSettingsRequest.getOpamp().getCertificateRequest().toByteArray()
                        )
                )
        );
    }

    private String convert(Anyvalue.AnyValue value) {
        if (Objects.isNull(value)) return "";
        // convert to string
        if (value.hasStringValue()) return value.getStringValue();
        if (value.hasIntValue()) return String.valueOf(value.getIntValue());
        if (value.hasBoolValue()) return String.valueOf(value.getBoolValue());
        if (value.hasDoubleValue()) return String.valueOf(value.getDoubleValue());
        if (value.hasArrayValue()) {
            return value.getArrayValue().getValuesList().stream().map(this::convert).collect(Collectors.joining(","));
        }
        if (value.hasKvlistValue()) {
            return value.getKvlistValue().getValuesList().stream().map(kv -> kv.getKey() + "=" + convert(kv.getValue())).collect(Collectors.joining(",", "{", "}"));
        }
        return null;
    }
}
