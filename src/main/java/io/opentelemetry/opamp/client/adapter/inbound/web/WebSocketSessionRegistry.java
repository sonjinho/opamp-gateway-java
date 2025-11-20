package io.opentelemetry.opamp.client.adapter.inbound.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class WebSocketSessionRegistry {

    private final Map<UUID, WebSocketSession> agentSessionMap = new ConcurrentHashMap<>();
    private final Map<String, UUID> agentSessionIdMap = new ConcurrentHashMap<>();

    public void register(UUID agentId, WebSocketSession session) {
        if (agentSessionMap.containsKey(agentId)) {
            WebSocketSession oldSession = agentSessionMap.get(agentId);
            if (!oldSession.getId().equals(session.getId())) {
                unregister(oldSession.getId());
            }
        } else {
            if (agentSessionIdMap.containsKey(session.getId())) {
                // session change the agentID
                UUID oldAgentId = agentSessionIdMap.get(session.getId());
                agentSessionMap.remove(oldAgentId);
                agentSessionIdMap.remove(session.getId());
            }
            agentSessionMap.put(agentId, session);
            agentSessionIdMap.put(session.getId(), agentId);
        }
    }

    public void unregister(String sessionId) {
        UUID agentId = agentSessionIdMap.get(sessionId);

        if (agentId != null) {
            agentSessionMap.remove(agentId);
            agentSessionIdMap.remove(sessionId);
            log.info("Unregistered session for targetId: {}", agentId);
        } else {
            log.warn("Could not find targetId for sessionId: {}", sessionId);
        }
    }

    public Optional<WebSocketSession> getSession(UUID agentId) {
        return Optional.ofNullable(agentSessionMap.get(agentId));
    }

    public Collection<WebSocketSession> getSessions() {
        return agentSessionMap.values();
    }

    public Collection<UUID> connectedAgents() {
        return agentSessionMap.entrySet().stream().filter(entry -> entry.getValue().isOpen()).map(Map.Entry::getKey).toList();
    }
}
