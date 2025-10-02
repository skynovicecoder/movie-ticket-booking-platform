package com.company.mtbp.common.kafka.consumers;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

class BaseKafkaConsumerTest {

    private TestConsumer consumer;
    private ListAppender<ILoggingEvent> listAppender;
    private AtomicReference<JsonNode> handledNode;

    @BeforeEach
    void setUp() {
        handledNode = new AtomicReference<>();

        consumer = new TestConsumer(new ObjectMapper(), handledNode);

        Logger logger = (Logger) LoggerFactory.getLogger(BaseKafkaConsumer.class);
        listAppender = new ListAppender<>();
        listAppender.start();

        logger.detachAndStopAllAppenders();
        logger.addAppender(listAppender);
    }

    private void assertLogContains(Level level, String substring) {
        assertThat(listAppender.list)
                .anySatisfy(event -> {
                    assertThat(event.getLevel()).isEqualTo(level);
                    assertThat(event.getFormattedMessage()).contains(substring);
                });
    }

    @Test
    void testConsume_ValidJson() {
        String message = """
                {
                  "id": 123,
                  "name": "John Doe"
                }
                """;

        consumer.consume(message);

        assertLogContains(Level.INFO, "Received raw message");

        assertThat(handledNode.get()).isNotNull();
        assertThat(handledNode.get().get("id").asInt()).isEqualTo(123);
        assertThat(handledNode.get().get("name").asText()).isEqualTo("John Doe");
    }

    @Test
    void testConsume_InvalidJson() {
        String invalidMessage = "not a json";

        consumer.consume(invalidMessage);

        assertLogContains(Level.ERROR, "Failed to parse message into JsonNode");

        assertThat(handledNode.get()).isNull();
    }

    private static class TestConsumer extends BaseKafkaConsumer {

        private final AtomicReference<JsonNode> lastNode;

        public TestConsumer(ObjectMapper objectMapper, AtomicReference<JsonNode> lastNode) {
            super(objectMapper);
            this.lastNode = lastNode;
        }

        @Override
        protected void handleMessage(JsonNode rootNode) {
            lastNode.set(rootNode);
        }
    }
}
