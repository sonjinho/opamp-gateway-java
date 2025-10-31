package io.opentelemetry.opamp.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.opentelemetry.opamp.agent.domain.AgentDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    public static final String AGENT_DOMAIN_CACHE = "agent_domain";

    @Value("${app.cache.version}")
    private String cacheVersion;
    @Value("${app.cache.default-ttl-minutes}")
    private long defaultTtlMinutes;

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory factory, ObjectMapper mapper) {

        GenericJackson2JsonRedisSerializer genericSerializer = new GenericJackson2JsonRedisSerializer(mapper);

        CacheKeyPrefix versionedPrefix = name -> name + ":" + cacheVersion + ":";

        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(defaultTtlMinutes))
                .disableCachingNullValues()
                .computePrefixWith(versionedPrefix)
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(genericSerializer));

        Jackson2JsonRedisSerializer<AgentDomain> serializer = new Jackson2JsonRedisSerializer<>(mapper, AgentDomain.class);

        RedisCacheConfiguration agentDomainConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(defaultTtlMinutes))
                .disableCachingNullValues()
                .computePrefixWith(versionedPrefix)
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer));

        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        cacheConfigurations.put(AGENT_DOMAIN_CACHE, agentDomainConfig);

        return RedisCacheManager.builder(factory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }
}
