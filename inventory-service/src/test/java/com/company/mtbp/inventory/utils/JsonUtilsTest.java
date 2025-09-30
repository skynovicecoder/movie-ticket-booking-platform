package com.company.mtbp.inventory.utils;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JsonUtilsTest {

    @Test
    void parseToMap_validJson_returnsMap() {
        String json = "{\"key1\": \"value1\", \"key2\": 2}";

        Map<String, Object> result = JsonUtils.parseToMap(json);

        assertEquals(2, result.size());
        assertEquals("value1", result.get("key1"));
        assertEquals(2, result.get("key2"));
    }

    @Test
    void parseToMap_invalidJson_throwsException() {
        String invalidJson = "{\"key1\": \"value1\", \"key2\": }";

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> JsonUtils.parseToMap(invalidJson));

        assertTrue(exception.getMessage().contains("Failed to parse JSON"));
    }

    @Test
    void parseToStringMap_validJson_returnsMap() {
        String json = "{\"key1\": \"value1\", \"key2\": \"value2\"}";

        Map<String, String> result = JsonUtils.parseToStringMap(json);

        assertEquals(2, result.size());
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
    }

    @Test
    void parseToIntMap_validJson_returnsMap() {
        String json = "{\"key1\": 1, \"key2\": 2}";

        Map<String, Integer> result = JsonUtils.parseToIntMap(json);

        assertEquals(2, result.size());
        assertEquals(1, result.get("key1"));
        assertEquals(2, result.get("key2"));
    }

    @Test
    void parseToIntMap_invalidJson_throwsException() {
        String invalidJson = "{\"key1\": \"value1\", \"key2\": 2}";

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> JsonUtils.parseToIntMap(invalidJson));

        assertTrue(exception.getMessage().contains("Failed to parse JSON"));
    }
}
