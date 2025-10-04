package com.company.mtbp.partner.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Value("${inventory.server.url}")
    String inventoryServerURL;

    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        return builder.baseUrl(inventoryServerURL).build();
    }
}