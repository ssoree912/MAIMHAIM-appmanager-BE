package com.sasoop.server.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;

@Converter(autoApply = true)
public class JsonConverter implements AttributeConverter<JsonNode, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(JsonNode jsonNode) {
        try {
            return objectMapper.writeValueAsString(jsonNode);
        } catch (IOException e) {
            throw new RuntimeException("Error converting JSON to String", e);
        }
    }

    @Override
    public JsonNode convertToEntityAttribute(String jsonString) {
        try {
            return objectMapper.readTree(jsonString);
        } catch (IOException e) {
            throw new RuntimeException("Error converting String to JSON", e);
        }
    }
}
