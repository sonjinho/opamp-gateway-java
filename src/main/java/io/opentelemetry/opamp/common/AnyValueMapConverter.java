package io.opentelemetry.opamp.common;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import opamp.proto.Anyvalue;

import java.util.HashMap;
import java.util.Map;

@Converter
public class AnyValueMapConverter implements AttributeConverter<Map<String, Anyvalue.AnyValue>, String> {

    @Override
    public String convertToDatabaseColumn(Map<String, Anyvalue.AnyValue> attribute) {
        if (attribute == null) return null;
        Map<String, String> jsonMap = new HashMap<>();
        attribute.forEach((k, v) -> {
            try {
                jsonMap.put(k, JsonFormat.printer().print(v));
            } catch (InvalidProtocolBufferException e) {
                throw new RuntimeException("Failed to serialize AnyValue", e);
            }
        });
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(jsonMap);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert map to JSON", e);
        }
    }

    @Override
    public Map<String, Anyvalue.AnyValue> convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            Map<String, String> jsonMap = new com.fasterxml.jackson.databind.ObjectMapper()
                    .readValue(dbData, new com.fasterxml.jackson.core.type.TypeReference<>() {});
            Map<String, Anyvalue.AnyValue> map = new HashMap<>();
            for (var entry : jsonMap.entrySet()) {
                Anyvalue.AnyValue.Builder builder = Anyvalue.AnyValue.newBuilder();
                JsonFormat.parser().merge(entry.getValue(), builder);
                map.put(entry.getKey(), builder.build());
            }
            return map;
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize AnyValue map", e);
        }
    }
}
