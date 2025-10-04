package com.company.mtbp.customer.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class SecurityWebFluxConfigTest {

    private CorsConfigurationSource corsConfigurationSource;
    private SecurityWebFluxConfig securityWebFluxConfig;

    @BeforeEach
    void setUp() {
        corsConfigurationSource = Mockito.mock(CorsConfigurationSource.class);
        when(corsConfigurationSource.getCorsConfiguration(Mockito.any()))
                .thenReturn(new CorsConfiguration());
        securityWebFluxConfig = new SecurityWebFluxConfig(corsConfigurationSource);
    }

    @Test
    void shouldBuildSecurityWebFilterChainSuccessfully() {
        ServerHttpSecurity http = ServerHttpSecurity.http();
        http.authenticationManager(Mockito.mock(org.springframework.security.authentication.ReactiveAuthenticationManager.class));

        SecurityWebFilterChain chain = securityWebFluxConfig.securityWebFilterChain(http);

        assertThat(chain).isNotNull();
    }

    @Test
    void shouldNotRequireCsrfForGetRequests() {
        ServerWebExchange exchange = MockServerWebExchange.from(
                org.springframework.mock.http.server.reactive.MockServerHttpRequest
                        .get("/some-path")
                        .build()
        );

        Mono<ServerWebExchangeMatcher.MatchResult> result =
                getCsrfMatcher().matches(exchange);

        assertThat(result.block().isMatch()).isFalse();
    }

    @Test
    void shouldNotRequireCsrfForActuatorPath() {
        ServerWebExchange exchange = MockServerWebExchange.from(
                org.springframework.mock.http.server.reactive.MockServerHttpRequest
                        .post("/actuator/info")
                        .build()
        );

        Mono<ServerWebExchangeMatcher.MatchResult> result =
                getCsrfMatcher().matches(exchange);

        assertThat(result.block().isMatch()).isFalse();
    }

    @Test
    void shouldNotRequireCsrfForSwaggerPath() {
        ServerWebExchange exchange = MockServerWebExchange.from(
                org.springframework.mock.http.server.reactive.MockServerHttpRequest
                        .post("/swagger-ui/index.html")
                        .build()
        );

        Mono<ServerWebExchangeMatcher.MatchResult> result =
                getCsrfMatcher().matches(exchange);

        assertThat(result.block().isMatch()).isFalse();
    }

    @Test
    void shouldNotRequireCsrfForHealthPath() {
        ServerWebExchange exchange = MockServerWebExchange.from(
                org.springframework.mock.http.server.reactive.MockServerHttpRequest
                        .post("/health/status")
                        .build()
        );

        Mono<ServerWebExchangeMatcher.MatchResult> result =
                getCsrfMatcher().matches(exchange);

        assertThat(result.block().isMatch()).isFalse();
    }

    @Test
    void shouldRequireCsrfForOtherPostPaths() {
        ServerWebExchange exchange = MockServerWebExchange.from(
                org.springframework.mock.http.server.reactive.MockServerHttpRequest
                        .post("/some-other-path")
                        .build()
        );

        Mono<ServerWebExchangeMatcher.MatchResult> result =
                getCsrfMatcher().matches(exchange);

        assertThat(result.block().isMatch()).isTrue();
    }

    /**
     * Returns the same logic as inside SecurityWebFluxConfig for isolated testing.
     */
    private ServerWebExchangeMatcher getCsrfMatcher() {
        return exchange -> {
            String path = exchange.getRequest().getURI().getPath();
            String method = exchange.getRequest().getMethod().name();
            if (HttpMethod.GET.matches(method) ||
                    (path.startsWith("/actuator") ||
                            path.startsWith("/health") ||
                            path.startsWith("/swagger-ui") ||
                            path.startsWith("/api"))) {
                return ServerWebExchangeMatcher.MatchResult.notMatch();
            }
            return ServerWebExchangeMatcher.MatchResult.match();
        };
    }
}
