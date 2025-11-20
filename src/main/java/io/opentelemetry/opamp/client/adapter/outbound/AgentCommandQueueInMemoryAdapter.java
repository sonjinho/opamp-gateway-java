package io.opentelemetry.opamp.client.adapter.outbound;

import io.opentelemetry.opamp.client.application.command.ServerToAgentEventCommand;
import io.opentelemetry.opamp.client.application.port.AgentCommandQueuePort;
import io.opentelemetry.opamp.client.domain.server.ServerToAgentDomain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class AgentCommandQueueInMemoryAdapter implements AgentCommandQueuePort {

    Map<UUID, Queue<ServerToAgentEventCommand>> commands = new ConcurrentHashMap<>();

    @Override
    public void enqueue(ServerToAgentEventCommand event) {
        Queue<ServerToAgentEventCommand> queue = commands.getOrDefault(event.targetId(), new ArrayBlockingQueue<>(1000));
        commands.put(event.targetId(), queue);
        queue.add(event);
    }

    @Override
    public Optional<ServerToAgentDomain> pollNextCommand(UUID agentId) {
        if (commands.containsKey(agentId)) {
            return Optional.ofNullable(commands.get(agentId).poll()).map(ServerToAgentEventCommand::toDomain);
        } else {
            return Optional.empty();
        }
    }

}
