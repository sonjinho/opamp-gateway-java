package io.opentelemetry.opamp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {
        "io.opentelemetry.opamp.agent.adapter.outbound.persistence.repository",
        "io.opentelemetry.opamp.gateway.adapter.outbound.persistence.jpa.repository"
})
public class JpaConfig {
}
