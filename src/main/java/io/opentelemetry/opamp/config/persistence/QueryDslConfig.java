package io.opentelemetry.opamp.config.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryDslConfig {

    private final EntityManager em;

    public QueryDslConfig(EntityManager em) {
        this.em = em;
    }

    // JPAQueryFactory를 빈으로 등록하여 QueryDSL을 사용할 수 있게 함
    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(em);
    }
}