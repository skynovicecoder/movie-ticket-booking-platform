package com.company.mtbp.booking.service;

import com.company.mtbp.common.kafka.consumers.BaseKafkaConsumer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BookingConsumer extends BaseKafkaConsumer {

    public BookingConsumer(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    @KafkaListener(
            topics = "${booking.kafka.topic}",
            groupId = "${booking.kafka.group}"
    )
    public void consume(String message) {
        super.consume(message);
    }

    @Override
    protected void handleMessage(JsonNode rootNode) {
        Long id = rootNode.get("id").asLong();
        String status = rootNode.get("status").asText();
        Double amount = rootNode.get("totalAmount").asDouble();
        Long customerId = rootNode.get("customerId").asLong();
        Long showId = rootNode.get("showId").asLong();

        log.info("Parsed BookingEvent -> id={}, status={}, totalAmount={}, customerId={}, showId={}",
                id, status, amount, customerId, showId);

        // TODO: Add booking-specific business logic here
    }
}