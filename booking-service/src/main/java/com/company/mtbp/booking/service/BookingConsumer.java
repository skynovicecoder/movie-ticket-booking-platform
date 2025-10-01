package com.company.mtbp.booking.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BookingConsumer {

    private final ObjectMapper objectMapper;

    public BookingConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = "${booking.kafka.topic}",
            groupId = "${booking.kafka.group}"
    )
    public void consume(String message) {
        log.info("Booking Service Received raw message: {}", message);

        try {
            JsonNode rootNode = objectMapper.readTree(message);

            Long id = rootNode.get("id").asLong();
            String status = rootNode.get("status").asText();
            Double amount = rootNode.get("totalAmount").asDouble();
            Long customerId = rootNode.get("customerId").asLong();
            Long showId = rootNode.get("showId").asLong();

            log.info("Parsed from JsonNode -> id={}, status={}, totalAmount={}, customerId={}, showId={}", id, status, amount, customerId, showId);

        } catch (Exception e) {
            log.error("Failed to parse message into JsonNode : {}", e.getMessage());
        }
    }
}