package io.opentelemetry.opamp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class OpampApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpampApplication.class, args);
    }

}
