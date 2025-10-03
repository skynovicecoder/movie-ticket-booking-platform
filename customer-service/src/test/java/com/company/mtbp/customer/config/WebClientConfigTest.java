package com.company.mtbp.customer.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class WebClientConfigTest {

    @Test
    void testWebClientBeanCreation() {
        WebClientConfig config = new WebClientConfig();
        WebClient.Builder builder = WebClient.builder();
        WebClient webClient = config.webClient(builder);
        assertNotNull(webClient, "WebClient should not be null");
        assertNotNull(webClient.mutate().build(), "WebClient should be mutable and buildable");
    }
}
