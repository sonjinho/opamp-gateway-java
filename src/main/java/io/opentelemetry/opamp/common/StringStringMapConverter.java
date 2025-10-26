package io.opentelemetry.opamp.common;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.HashMap;
import java.util.Map;

@Converter
public class StringStringMapConverter implements AttributeConverter<Map<String, String>, String> {

    @Override
    public String convertToDatabaseColumn(Map<String, String> attribute) {
        if (attribute == null) return null;
        Map<String, String> jsonMap = new HashMap<>(attribute);
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(jsonMap);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert map to JSON", e);
        }
    }

    @Override
    public Map<String, String> convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            Map<String, String> jsonMap = new com.fasterxml.jackson.databind.ObjectMapper()
                    .readValue(dbData, new com.fasterxml.jackson.core.type.TypeReference<>() {});
            //                Anyvalue.AnyValue.Builder builder = Anyvalue.AnyValue.newBuilder();
            //                JsonFormat.parser().merge(entry.getValue(), builder);
            return new HashMap<>(jsonMap);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize AnyValue map", e);
        }
    }
}
