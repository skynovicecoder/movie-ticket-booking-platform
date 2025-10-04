package com.company.mtbp.partner.config;

import io.github.resilience4j.retry.Retry;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

class ResilienceConfigTest {

    @Test
    void testTheatreSeatRetryBeanCreation() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ResilienceConfig.class);

        Retry retry = context.getBean("theatreSeatRetry", Retry.class);

        assertNotNull(retry, "theatreSeatRetry bean should not be null");
        assertEquals("theatreSeatService", retry.getName(), "Retry name should match configuration");

        context.close();
    }

    @Test
    void testTheatreShowRetryBeanCreation() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ResilienceConfig.class);

        Retry retry = context.getBean("theatreShowRetry", Retry.class);

        assertNotNull(retry, "theatreShowRetry bean should not be null");
        assertEquals("theatreShowService", retry.getName(), "Retry name should match configuration");

        context.close();
    }

    @Test
    void testBothBeansAreDistinctInstances() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ResilienceConfig.class);

        Retry seatRetry = context.getBean("theatreSeatRetry", Retry.class);
        Retry showRetry = context.getBean("theatreShowRetry", Retry.class);

        assertNotSame(seatRetry, showRetry, "Both retry beans should be different instances");
        assertEquals("theatreSeatService", seatRetry.getName());
        assertEquals("theatreShowService", showRetry.getName());

        context.close();
    }
}
