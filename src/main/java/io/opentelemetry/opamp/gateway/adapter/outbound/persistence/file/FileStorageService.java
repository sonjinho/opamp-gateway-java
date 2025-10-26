package io.opentelemetry.opamp.gateway.adapter.outbound.persistence.file;

import io.opentelemetry.opamp.gateway.domain.agent.AgentToServerDomain;

import java.util.List;
import java.util.UUID;

public interface FileStorageService {
    void save(AgentToServerDomain domain);

    // read recent sequence
    AgentToServerDomain read(UUID instanceUid);

    // read specific sequence
    AgentToServerDomain read(UUID instanceUid, long seqNum);

    // read all files under given UUID
    List<AgentToServerDomain> readAll(UUID instanceUid);
}
