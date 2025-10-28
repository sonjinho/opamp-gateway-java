package io.opentelemetry.opamp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories(basePackages = "io.opentelemetry.opamp.gateway.adapter.outbound.persistence.redis")
public class RedisConfig {
}
