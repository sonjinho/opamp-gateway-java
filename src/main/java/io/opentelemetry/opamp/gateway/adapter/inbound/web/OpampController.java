package io.opentelemetry.opamp.gateway.adapter.inbound.web;

import io.opentelemetry.opamp.gateway.application.service.OpampService;
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

    private final OpampService service;

    // Post to /v1/opamp body binary protobuf
    @PostMapping(value = "/v1/opamp", produces = MediaType.APPLICATION_PROTOBUF_VALUE, consumes = MediaType.APPLICATION_PROTOBUF_VALUE)
    public Opamp.ServerToAgent opamp(@RequestBody() Opamp.AgentToServer requestBody) {
        return service.processRequest(requestBody);
    }

}
