package io.opentelemetry.opamp.telemetry.trace.adapter.outbound.tempo.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@ConditionalOnProperty(prefix = "telemetry.trace", name = "type", havingValue = "tempo")
@Configuration
public class TempoConfiguration {

    @Value("${telemetry.trace.tempo.endpoint}")
    private String endpoint;

    @Bean("tempoRestClient")
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(endpoint)
                .build();
    }
}
