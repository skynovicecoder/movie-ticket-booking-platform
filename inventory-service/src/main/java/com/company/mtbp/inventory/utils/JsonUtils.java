package com.company.mtbp.inventory.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class JsonUtils {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Parse JSON string to Map<String, Object>
     */
    public Map<String, Object> parseToMap(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON: " + json, e);
        }
    }

    /**
     * Parse JSON string to Map<String, String>
     */
    public Map<String, String> parseToStringMap(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, String>>() {
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON: " + json, e);
        }
    }

    /**
     * Parse JSON string to Map<String, Integer>
     */
    public Map<String, Integer> parseToIntMap(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Integer>>() {
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON: " + json, e);
        }
    }
}
