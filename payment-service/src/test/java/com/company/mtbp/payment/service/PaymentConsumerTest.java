package com.company.mtbp.payment.service;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentConsumerTest {

    private PaymentConsumer paymentConsumer;
    private ListAppender<ILoggingEvent> listAppender;

    @BeforeEach
    void setUp() {
        paymentConsumer = new PaymentConsumer(new ObjectMapper());

        paymentConsumer = new PaymentConsumer(new ObjectMapper());

        Logger baseLogger = (Logger) LoggerFactory.getLogger("com.company.mtbp.common.kafka.consumers.BaseKafkaConsumer");
        Logger paymentLogger = (Logger) LoggerFactory.getLogger(PaymentConsumer.class);

        listAppender = new ListAppender<>();
        listAppender.start();

        baseLogger.detachAndStopAllAppenders();
        baseLogger.addAppender(listAppender);

        paymentLogger.detachAndStopAllAppenders();
        paymentLogger.addAppender(listAppender);
    }

    private List<ILoggingEvent> getLogs() {
        return listAppender.list;
    }

    private void assertLogContains(Level level, String substring) {
        assertThat(getLogs())
                .anySatisfy(log -> {
                    assertThat(log.getLevel()).isEqualTo(level);
                    assertThat(log.getFormattedMessage()).contains(substring);
                });
    }

    @Test
    void testConsume_ValidMessage() {
        String message = """
                {
                  "id": 101,
                  "status": "SUCCESS",
                  "totalAmount": 123.45,
                  "customerId": 1,
                  "showId": 1
                }
                """;

        paymentConsumer.consume(message);

        assertLogContains(Level.INFO, "Parsed PaymentEvent -> id=101, status=SUCCESS, totalAmount=123.45, customerId=1, showId=1");
        assertLogContains(Level.INFO, "Received raw message");
    }

    @Test
    void testConsume_MissingOptionalField() {
        String message = """
                {
                  "id": 102,
                  "status": "FAILED",
                  "totalAmount": 99.99,
                  "customerId": 1
                }
                """;

        paymentConsumer.consume(message);

        assertLogContains(Level.ERROR, "Failed to parse message into JsonNode");

        assertLogContains(Level.INFO, "Received raw message");
    }

    @Test
    void testConsume_InvalidJson() {
        String invalidMessage = "Not a JSON string";

        paymentConsumer.consume(invalidMessage);

        assertLogContains(Level.ERROR, "Failed to parse message into JsonNode");
        assertLogContains(Level.INFO, "Received raw message");
    }
}
