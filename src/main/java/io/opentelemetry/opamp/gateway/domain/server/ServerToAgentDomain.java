package io.opentelemetry.opamp.gateway.domain.server;

import java.util.UUID;

public record ServerToAgentDomain(
        UUID instanceId,
        ServerErrorResponseDomain serverErrorResponse,
        AgentRemoteConfigDomain agentRemoteConfig,
        ConnectionSettingsOffersDomain connectionSettingsOffers,
        PackagesAvailableDomain packagesAvailable,
        Long flags,
        Long capabilities,
        AgentIdentificationDomain agentIdentification,
        ServerToAgentCommandDomain command,
        CustomCapabilitiesDomain customCapabilities,
        CustomMessageDomain customMessage
) {
}
