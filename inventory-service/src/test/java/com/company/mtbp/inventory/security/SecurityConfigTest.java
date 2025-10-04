package com.company.mtbp.inventory.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class SecurityConfigTest {

    @Test
    void securityFilterChain_shouldBuildSuccessfully() throws Exception {
        SecurityConfig config = new SecurityConfig();
        HttpSecurity http = mock(HttpSecurity.class, org.mockito.Mockito.RETURNS_DEEP_STUBS);

        SecurityFilterChain chain = config.securityFilterChain(http);
        assertThat(chain).isNotNull();
    }

}
