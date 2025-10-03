package com.company.mtbp.customer.config;

import io.github.resilience4j.retry.Retry;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ResilienceConfigTest {

    @Test
    void testBookingRetryBeanCreation() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ResilienceConfig.class);

        Retry retryBean = context.getBean("bookingRetry", Retry.class);

        assertNotNull(retryBean);

        assertEquals("paymentService", retryBean.getName());

        context.close();
    }
}
