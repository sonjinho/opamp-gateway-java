package io.opentelemetry.opamp.gateway.adapter.inbound.web;

import io.opentelemetry.opamp.gateway.application.usecase.OpampUseCase;
import io.opentelemetry.opamp.gateway.mapper.AgentToServerMapper;
import io.opentelemetry.opamp.gateway.mapper.ServerToAgentMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import opamp.proto.Opamp;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class OpampController {

    private final OpampUseCase service;
    private final AgentToServerMapper agentToServerMapper;
    private final ServerToAgentMapper serverToAgentMapper;

    // Post to /v1/opamp body binary protobuf
    @PostMapping(value = "/api/v1/opamp", produces = MediaType.APPLICATION_PROTOBUF_VALUE, consumes = MediaType.APPLICATION_PROTOBUF_VALUE)
    public Opamp.ServerToAgent opamp(@RequestBody() Opamp.AgentToServer requestBody) {
        var agentToServer = agentToServerMapper.mapperToDomain(requestBody);
        log.info("Agent To Server Request {}:", agentToServer);
        var serverToAgent = service.processRequest(agentToServer);
        return serverToAgentMapper.mapperToProto(serverToAgent);
    }

}
