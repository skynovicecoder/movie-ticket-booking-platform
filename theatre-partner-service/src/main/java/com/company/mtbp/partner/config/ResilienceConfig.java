package com.company.mtbp.partner.config;

import io.github.resilience4j.retry.Retry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResilienceConfig {

    @Bean
    public Retry theatreSeatRetry() {
        return Retry.ofDefaults("theatreSeatService");
    }

    @Bean
    public Retry theatreShowRetry() {
        return Retry.ofDefaults("theatreShowService");
    }
}