package io.opentelemetry.opamp.telemetry.log.adapter.outbound.loki.dto;

import io.opentelemetry.opamp.common.util.OTELUtil;
import io.opentelemetry.opamp.telemetry.log.domain.OtelLogRecord;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public record LokiResponseDTO(
        String status,
        LokiDataDTO data
) {
    private static Map<String, String> extractAndRemoveResourceLabels(Map<String, String> stream) {
        Map<String, String> resource = new HashMap<>();

        Iterator<Map.Entry<String, String>> iterator = stream.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            String key = entry.getKey();
            String value = entry.getValue();

            if (OTELUtil.INSTANCE.RESOURCE_KEYS.contains(key)) {
                switch (key) {
                    case "job":
                        if (!resource.containsKey("service.name")) {
                            resource.put("service.name", value);
                        }
                        break;
                    case "instance":
                        if (!resource.containsKey("service.instance.id")) {
                            resource.put("service.instance.id", value);
                        }
                        if (!resource.containsKey("service.namespace")) {
                            resource.put("host.name", value);
                        }
                        break;
                    case "namespace":
                        resource.put("k8s.namespace.name", value);
                        break;
                    case "pod":
                        resource.put("k8s.pod.name", value);
                        break;
                    default:
                        resource.put(key, value);
                        break;
                }
                iterator.remove();
            }
        }

        resource.put("telemetry.sdk.name", "loki");
        return resource;
    }

    private static Map<String, String> extractAttributes(Map<String, String> stream) {
        return stream.entrySet().stream()
                .filter(e -> !Set.of("level", "trace_id", "span_id").contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public List<OtelLogRecord> toEntries() {
        List<OtelLogRecord> entries = new ArrayList<>(Math.max(data.result().size(), 10));
        for (LokiResultDTO result : data.result()) {
            if (result.values() == null) continue;
            Map<String, String> stream = new HashMap<>(result.stream());

            Map<String, String> resource = extractAndRemoveResourceLabels(stream);
            Map<String, String> baseAttributes = extractAttributes(stream);

            for (List<String> value : result.values()) {
                if (value.size() < 2) continue;
                String timestampNano = value.getFirst();
                String message = value.getLast();
                Instant ts = Instant.ofEpochSecond(
                        Long.parseLong(timestampNano) / 1_000_000_000L,
                        Long.parseLong(timestampNano) % 1_000_000_000L

                );
                final String traceId = stream.get("trace_id");
                final String spanId = stream.get("span_id");
                final String severityText = stream.getOrDefault("level", "INFO");

                final var entry = new OtelLogRecord(
                        ts,
                        severityText,
                        message,
                        baseAttributes,
                        resource,
                        traceId,
                        spanId
                );
                entries.add(
                        entry
                );
            }
        }

        return entries;
    }
}
