package com.company.mtbp.common.kafka.consumers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseKafkaConsumer {

    protected final ObjectMapper objectMapper;

    protected BaseKafkaConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void consume(String message) {
        log.info("Received raw message: {}", message);

        try {
            JsonNode rootNode = objectMapper.readTree(message);
            handleMessage(rootNode);
        } catch (Exception e) {
            log.error("Failed to parse message into JsonNode : {}", e.getMessage());
        }
    }

    protected abstract void handleMessage(JsonNode rootNode);
}
