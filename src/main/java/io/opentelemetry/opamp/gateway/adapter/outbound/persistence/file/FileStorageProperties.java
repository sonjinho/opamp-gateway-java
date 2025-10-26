package io.opentelemetry.opamp.gateway.adapter.outbound.persistence.file;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "storage.file")
public class FileStorageProperties {
    private Path basePath = Path.of("./data/opamp");
}
