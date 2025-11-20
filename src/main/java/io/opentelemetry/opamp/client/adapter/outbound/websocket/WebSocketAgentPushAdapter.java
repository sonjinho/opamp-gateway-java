package io.opentelemetry.opamp.client.adapter.outbound.websocket;

import io.opentelemetry.opamp.client.adapter.inbound.web.WebSocketSessionRegistry;
import io.opentelemetry.opamp.client.application.port.AgentPushPort;
import io.opentelemetry.opamp.client.domain.server.ConnectionSettingsOffersDomain;
import io.opentelemetry.opamp.client.domain.server.ServerToAgentDomain;
import io.opentelemetry.opamp.client.domain.server.ServerToAgentFlags;
import io.opentelemetry.opamp.client.mapper.ServerToAgentMapper;
import io.opentelemetry.opamp.common.util.OPAMPUtil;
import io.opentelemetry.opamp.owntelemetry.domain.OwnTelemetrySetting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketAgentPushAdapter implements AgentPushPort {

    private final WebSocketSessionRegistry registry;
    private final ServerToAgentMapper mapper;

    @Override
    public void push(UUID agentId, ServerToAgentDomain serverToAgentDomain) {
        Optional<WebSocketSession> session = registry.getSession(agentId);
        if (session.isPresent()) {
            byte[] responseBytes = OPAMPUtil.INSTANCE.encodeOpampWS(mapper.mapperToProto(serverToAgentDomain));
            try {
                session.get().sendMessage(new BinaryMessage(responseBytes));
            } catch (IOException e) {
                log.error("Failed to send OpAMP message to agent {}", agentId, e);
            }
        }
    }

    @Override
    public void batchUpdateOwnTelemetrySetting(OwnTelemetrySetting telemetrySetting) {
        var serverToAgent = ServerToAgentDomain.builder()
                .flags(ServerToAgentFlags.UNSPECIFIED.val())
                .connectionSettingsOffers(ConnectionSettingsOffersDomain.from(telemetrySetting))
                .build();
        byte[] responseBytes = OPAMPUtil.INSTANCE.encodeOpampWS(mapper.mapperToProto(serverToAgent));
        List<String> failedAgents = new ArrayList<>(10);
        registry.getSessions().parallelStream().forEach(session -> {
            try {
                session.sendMessage(new BinaryMessage(responseBytes));
            } catch (IOException e) {
                failedAgents.add(session.getId());
                log.error("Failed to send OpAMP message to agent {}", session.getId(), e);
            }
        });
        failedAgents.forEach(registry::unregister);
    }
}
