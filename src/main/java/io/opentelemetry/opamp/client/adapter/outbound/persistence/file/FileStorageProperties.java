package io.opentelemetry.opamp.client.adapter.outbound.persistence.file;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;

@Getter
@Setter
@Configuration
public class FileStorageProperties {
    private Path basePath = Path.of("./data/opamp");
}
