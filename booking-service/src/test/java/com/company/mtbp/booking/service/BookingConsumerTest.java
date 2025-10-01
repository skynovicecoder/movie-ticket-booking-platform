package com.company.mtbp.booking.service;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BookingConsumerTest {

    private BookingConsumer bookingConsumer;
    private ListAppender<ILoggingEvent> listAppender;

    @BeforeEach
    void setUp() {
        bookingConsumer = new BookingConsumer(new ObjectMapper());

        Logger logger = (Logger) LoggerFactory.getLogger(BookingConsumer.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        logger.detachAndStopAllAppenders();
        logger.addAppender(listAppender);
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

        List<ILoggingEvent> logs = listAppender.list;

        assertThat(logs).anyMatch(e -> e.getFormattedMessage()
                .contains("Parsed from JsonNode -> id=101, status=CONFIRMED, totalAmount=250.75, customerId=1, showId=1"));

    }

    @Test
    void testConsume_MissingCustomer() {
        String message = """
                {
                  "id": 202,
                  "status": "PENDING",
                  "totalAmount": 99.99,
                  "customerId": 1,
                  "showId": 1
                }
                """;

        bookingConsumer.consume(message);

        List<ILoggingEvent> logs = listAppender.list;

        assertThat(logs).anyMatch(e -> e.getFormattedMessage()
                .contains("Parsed from JsonNode -> id=202, status=PENDING, totalAmount=99.99"));

        assertThat(logs).noneMatch(e -> e.getFormattedMessage().contains("Customer Name"));
    }

    @Test
    void testConsume_InvalidJson() {
        String invalidMessage = "This is not a JSON string";

        bookingConsumer.consume(invalidMessage);

        List<ILoggingEvent> logs = listAppender.list;

        assertThat(logs).anyMatch(e -> e.getFormattedMessage()
                .contains("Failed to parse message into JsonNode"));
    }
}
