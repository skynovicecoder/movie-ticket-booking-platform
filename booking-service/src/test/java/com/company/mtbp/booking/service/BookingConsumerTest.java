package com.company.mtbp.booking.service;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

class BookingConsumerTest {

    private BookingConsumer bookingConsumer;
    private ListAppender<ILoggingEvent> listAppender;

    @BeforeEach
    void setUp() {
        bookingConsumer = new BookingConsumer(new ObjectMapper());

        Logger bookingLogger = (Logger) LoggerFactory.getLogger(BookingConsumer.class);
        Logger baseLogger = (Logger) LoggerFactory.getLogger("com.company.mtbp.common.kafka.consumers.BaseKafkaConsumer");

        listAppender = new ListAppender<>();
        listAppender.start();

        bookingLogger.detachAndStopAllAppenders();
        bookingLogger.addAppender(listAppender);

        baseLogger.detachAndStopAllAppenders();
        baseLogger.addAppender(listAppender);
    }

    private void assertLogContains(Level level, String substring) {
        assertThat(listAppender.list)
                .anySatisfy(logEvent -> {
                    assertThat(logEvent.getLevel()).isEqualTo(level);
                    assertThat(logEvent.getFormattedMessage()).contains(substring);
                });
    }

    @Test
    void testConsume_ValidMessage() {
        String message = """
                {
                  "id": 101,
                  "status": "CONFIRMED",
                  "totalAmount": 250.75,
                  "customerId": 1,
                  "showId": 1
                }
                """;

        bookingConsumer.consume(message);

        assertLogContains(Level.INFO, "Received raw message");
        assertLogContains(Level.INFO, "Parsed BookingEvent -> id=101, status=CONFIRMED, totalAmount=250.75, customerId=1, showId=1");
    }

    @Test
    void testConsume_MissingCustomer() {
        String message = """
                {
                  "id": 202,
                  "status": "PENDING",
                  "totalAmount": 99.99,
                  "showId": 1
                }
                """;

        bookingConsumer.consume(message);

        assertLogContains(Level.INFO, "Received raw message");
        assertLogContains(Level.ERROR, "Failed to parse message into JsonNode");
        assertThat(listAppender.list)
                .noneMatch(e -> e.getFormattedMessage().contains("Customer Name"));
    }

    @Test
    void testConsume_InvalidJson() {
        String invalidMessage = "This is not a JSON string";

        bookingConsumer.consume(invalidMessage);

        assertLogContains(Level.ERROR, "Failed to parse message into JsonNode");
    }
}
