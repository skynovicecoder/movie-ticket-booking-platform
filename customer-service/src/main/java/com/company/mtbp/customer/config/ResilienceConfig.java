package com.company.mtbp.customer.config;

import io.github.resilience4j.retry.Retry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResilienceConfig {

    @Bean
    public Retry bookingRetry() {
        return Retry.ofDefaults("paymentService");
    }
}