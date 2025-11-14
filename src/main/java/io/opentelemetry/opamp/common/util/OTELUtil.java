package io.opentelemetry.opamp.common.util;

import java.util.Set;

public enum OTELUtil {
    INSTANCE;

    public final Set<String> RESOURCE_KEYS = Set.of(
            "job", "instance", "namespace", "pod", "service",
            "service.name", "service.namespace", "service.instance.id",
            "host.name", "k8s.namespace.name", "k8s.pod.name"
    );
}
