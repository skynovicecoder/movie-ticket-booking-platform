package com.company.mtbp.payment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentConsumer {

    private final ObjectMapper objectMapper;

    public PaymentConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = "${payment.kafka.topic}",
            groupId = "${payment.kafka.group}"
    )
    public void consume(String message) {
        log.info("Payment Service Received raw message: {}", message);

        try {
            JsonNode rootNode = objectMapper.readTree(message);

            Long id = rootNode.get("id").asLong();
            String status = rootNode.get("status").asText();
            Double amount = rootNode.get("totalAmount").asDouble();

            log.info("Parsed from JsonNode -> id={}, status={}, totalAmount={}", id, status, amount);

            JsonNode customerNode = rootNode.get("customer");
            if (customerNode != null) {
                String customerName = customerNode.get("name").asText();
                log.info("Customer name: {}", customerName);
            }

        } catch (Exception e) {
            log.error("Failed to parse message into JsonNode : {}", e.getMessage());
        }
    }
}