package io.opentelemetry.opamp.telemetry.metric.adapter.outbound.prometheus.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@ConditionalOnProperty(prefix = "telemetry.metric", name = "type", havingValue = "prometheus")
@Configuration
public class PrometheusConfiguration {

    @Value("${telemetry.metric.prometheus.endpoint}")
    private String endpoint;

    @Bean("prometheusRestClient")
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(endpoint)
                .build();
    }
}
