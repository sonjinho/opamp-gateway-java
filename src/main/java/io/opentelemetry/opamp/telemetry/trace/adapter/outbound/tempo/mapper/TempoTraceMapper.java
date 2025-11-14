package io.opentelemetry.opamp.telemetry.trace.adapter.outbound.tempo.mapper;

import com.google.protobuf.ByteString;
import io.opentelemetry.opamp.telemetry.trace.domain.*;
import io.opentelemetry.proto.common.v1.AnyValue;
import io.opentelemetry.proto.common.v1.InstrumentationScope;
import io.opentelemetry.proto.common.v1.KeyValue;
import io.opentelemetry.proto.resource.v1.Resource;
import io.opentelemetry.proto.trace.v1.ScopeSpans;
import io.opentelemetry.proto.trace.v1.Span;
import io.opentelemetry.proto.trace.v1.Status;
import io.opentelemetry.proto.trace.v1.TracesData;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TempoTraceMapper {

    public OtelTraceRecord toOtelTraceRecord(Optional<TracesData> tracesData) {
        return tracesData.map(this::toOtelTraceRecord).orElse(null);
    }

    private OtelTraceRecord toOtelTraceRecord(TracesData tracesData) {
        return new OtelTraceRecord(
                tracesData.getResourceSpansList().stream()
                        .map(this::toOtelResourceSpans)
                        .collect(Collectors.toList())
        );
    }

    private OtelResourceSpans toOtelResourceSpans(io.opentelemetry.proto.trace.v1.ResourceSpans resourceSpans) {
        return new OtelResourceSpans(
                toOtelResource(resourceSpans.getResource()),
                resourceSpans.getScopeSpansList().stream()
                        .map(this::toOtelScopeSpans)
                        .collect(Collectors.toList()),
                resourceSpans.getSchemaUrl()
        );
    }

    private OtelResource toOtelResource(Resource resource) {
        return new OtelResource(
                resource.getAttributesList().stream()
                        .map(this::toOtelAttribute)
                        .collect(Collectors.toList())
        );
    }

    private OtelScopeSpans toOtelScopeSpans(ScopeSpans scopeSpans) {
        return new OtelScopeSpans(
                toOtelInstrumentationScope(scopeSpans.getScope()),
                scopeSpans.getSpansList().stream()
                        .map(this::toOtelSpan)
                        .collect(Collectors.toList()),
                scopeSpans.getSchemaUrl()
        );
    }

    private OtelInstrumentationScope toOtelInstrumentationScope(InstrumentationScope scope) {
        return new OtelInstrumentationScope(
                scope.getName(),
                scope.getVersion(),
                scope.getAttributesList().stream()
                        .map(this::toOtelAttribute)
                        .collect(Collectors.toList()),
                scope.getDroppedAttributesCount()
        );
    }

    private OtelSpan toOtelSpan(Span span) {
        return new OtelSpan(
                toHexString(span.getTraceId()),
                toHexString(span.getSpanId()),
                span.getTraceState(),
                toHexString(span.getParentSpanId()),
                span.getFlags(),
                span.getName(),
                span.getKind().name(),
                span.getStartTimeUnixNano(),
                span.getEndTimeUnixNano(),
                span.getAttributesList().stream()
                        .map(this::toOtelAttribute)
                        .collect(Collectors.toList()),
                span.getDroppedAttributesCount(),
                span.getEventsList().stream()
                        .map(this::toOtelEvent)
                        .collect(Collectors.toList()),
                span.getDroppedEventsCount(),
                span.getLinksList().stream()
                        .map(this::toOtelLink)
                        .collect(Collectors.toList()),
                span.getDroppedLinksCount(),
                toOtelStatus(span.getStatus())
        );
    }

    private OtelAttribute toOtelAttribute(KeyValue keyValue) {
        return new OtelAttribute(
                keyValue.getKey(),
                toOtelValue(keyValue.getValue())
        );
    }

    private OtelValue toOtelValue(AnyValue anyValue) {
        return new OtelValue(
                anyValue.hasStringValue() ? anyValue.getStringValue() : null,
                anyValue.hasBoolValue() ? anyValue.getBoolValue() : null,
                anyValue.hasIntValue() ? anyValue.getIntValue() : null,
                anyValue.hasDoubleValue() ? anyValue.getDoubleValue() : null,
                anyValue.hasArrayValue() ? anyValue.getArrayValue().getValuesList().stream().map(this::toOtelValue).collect(Collectors.toList()) : null,
                anyValue.hasKvlistValue() ? anyValue.getKvlistValue().getValuesList().stream().map(this::toOtelAttribute).collect(Collectors.toList()) : null,
                anyValue.hasBytesValue() ? anyValue.getBytesValue().toStringUtf8() : null
        );
    }

    private OtelEvent toOtelEvent(Span.Event event) {
        return new OtelEvent(
                event.getTimeUnixNano(),
                event.getName(),
                event.getAttributesList().stream()
                        .map(this::toOtelAttribute)
                        .collect(Collectors.toList()),
                event.getDroppedAttributesCount()
        );
    }

    private OtelLink toOtelLink(Span.Link link) {
        return new OtelLink(
                toHexString(link.getTraceId()),
                toHexString(link.getSpanId()),
                link.getTraceState(),
                link.getAttributesList().stream()
                        .map(this::toOtelAttribute)
                        .collect(Collectors.toList()),
                link.getDroppedAttributesCount(),
                link.getFlags()
        );
    }


    private OtelStatus toOtelStatus(Status status) {
        return new OtelStatus(
                status.getMessage(),
                status.getCode().name()
        );
    }

    private String toHexString(ByteString byteString) {
        if (byteString.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder(byteString.size() * 2);
        for (byte b : byteString) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
