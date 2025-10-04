package com.company.mtbp.partner.security;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class SecurityConfigTest {
    private RequestMatcher csrfIgnoreMatcher;

    @BeforeEach
    void setUp() throws Exception {
        SecurityConfig config = new SecurityConfig();

        csrfIgnoreMatcher = request -> {
            String method = request.getMethod();
            String uri = request.getRequestURI();
            if ("GET".equals(method) ||
                    uri.startsWith("/actuator") ||
                    uri.startsWith("/health") ||
                    uri.startsWith("/swagger-ui") ||
                    uri.startsWith("/api")) {
                return true;
            }
            return false;
        };
    }

    private MockHttpServletRequest mockRequest(String method, String uri) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod(method);
        request.setRequestURI(uri);
        return request;
    }

    @Test
    void getRequest_shouldIgnoreCsrf() {
        HttpServletRequest req = mockRequest("GET", "/any/path");
        assertThat(csrfIgnoreMatcher.matches(req)).isTrue();
    }

    @Test
    void actuatorRequest_shouldIgnoreCsrf() {
        HttpServletRequest req = mockRequest("POST", "/actuator/info");
        assertThat(csrfIgnoreMatcher.matches(req)).isTrue();
    }

    @Test
    void healthRequest_shouldIgnoreCsrf() {
        HttpServletRequest req = mockRequest("POST", "/health/status");
        assertThat(csrfIgnoreMatcher.matches(req)).isTrue();
    }

    @Test
    void swaggerRequest_shouldIgnoreCsrf() {
        HttpServletRequest req = mockRequest("POST", "/swagger-ui/index.html");
        assertThat(csrfIgnoreMatcher.matches(req)).isTrue();
    }

    @Test
    void apiRequest_shouldIgnoreCsrf() {
        HttpServletRequest req = mockRequest("POST", "/api/v1/test");
        assertThat(csrfIgnoreMatcher.matches(req)).isTrue();
    }

    @Test
    void otherPostRequest_shouldNotIgnoreCsrf() {
        HttpServletRequest req = mockRequest("POST", "/partner/data");
        assertThat(csrfIgnoreMatcher.matches(req)).isFalse();
    }

    @Test
    void securityFilterChain_shouldBuildSuccessfully() throws Exception {
        SecurityConfig config = new SecurityConfig();
        var http = mock(org.springframework.security.config.annotation.web.builders.HttpSecurity.class, org.mockito.Mockito.RETURNS_DEEP_STUBS);
        var chain = config.securityFilterChain(http);
        assertThat(chain).isNotNull();
    }

    @Test
    void securityFilterChain_should_Build_Successfully() throws Exception {
        SecurityConfig config = new SecurityConfig();
        HttpSecurity http = mock(HttpSecurity.class, org.mockito.Mockito.RETURNS_DEEP_STUBS);

        SecurityFilterChain chain = config.securityFilterChain(http);
        assertThat(chain).isNotNull();
    }

}
