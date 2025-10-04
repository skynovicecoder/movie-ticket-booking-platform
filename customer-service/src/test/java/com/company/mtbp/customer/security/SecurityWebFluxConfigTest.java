package com.company.mtbp.customer.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SecurityWebFluxConfigTest {

    @Autowired
    private SecurityWebFilterChain securityWebFilterChain;


    @Test
    void securityWebFilterChain_shouldBeLoaded() {
        assertThat(securityWebFilterChain).isNotNull();
    }
}
