package io.opentelemetry.opamp.telemetry.log.adapter.outbound.loki.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

// only enable log.type = loki
@Configuration
@ConditionalOnProperty(prefix = "telemetry.log", name = "type", havingValue = "loki")
public class LokiConfiguration {

    @Value("${telemetry.log.loki.endpoint:http://localhost:3100}")
    String endPoint;

    @Bean
    public RestClient lokiRestClient(
            RestClient.Builder builder
    ) {
        return builder
                .baseUrl(endPoint)
                .build();
    }
}