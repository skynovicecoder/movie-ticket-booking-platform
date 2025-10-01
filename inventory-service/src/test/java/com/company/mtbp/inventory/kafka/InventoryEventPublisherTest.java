package com.company.mtbp.inventory.kafka;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class InventoryEventPublisherTest {

    private KafkaTemplate<String, String> kafkaTemplate;
    private InventoryEventPublisher publisher;

    @BeforeEach
    void setUp() {
        kafkaTemplate = mock(KafkaTemplate.class);
        publisher = new InventoryEventPublisher(kafkaTemplate);

        try {
            var field = InventoryEventPublisher.class.getDeclaredField("topic");
            field.setAccessible(true);
            field.set(publisher, "test-topic");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testSendMessage_callsKafkaTemplateAndLogs() {
        String message = "Hello Kafka!";

        Logger logger = LoggerFactory.getLogger(InventoryEventPublisher.class);
        ch.qos.logback.classic.Logger logbackLogger = (ch.qos.logback.classic.Logger) logger;
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logbackLogger.addAppender(listAppender);

        publisher.sendMessage(message);

        verify(kafkaTemplate, times(1)).send("test-topic", message);

        boolean logFound = listAppender.list.stream()
                .anyMatch(event -> event.getFormattedMessage().contains("Sent message: Hello Kafka! to topic: test-topic"));
        assertThat(logFound).isTrue();
    }
}
