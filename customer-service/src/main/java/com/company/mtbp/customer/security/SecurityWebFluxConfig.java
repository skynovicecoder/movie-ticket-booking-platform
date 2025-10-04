package com.company.mtbp.customer.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityWebFluxConfig {
    private final CorsConfigurationSource corsConfigurationSource;

    @Autowired
    public SecurityWebFluxConfig(CorsConfigurationSource corsConfigurationSource) {
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(csrf -> csrf
                        .requireCsrfProtectionMatcher(new ServerWebExchangeMatcher() {
                            public Mono<MatchResult> matches(ServerWebExchange exchange) {
                                String path = exchange.getRequest().getURI().getPath();
                                String method = exchange.getRequest().getMethod().name();

                                // CSRF is disabled because this is a stateless API using JWT tokens
                                if (HttpMethod.GET.matches(method) ||
                                        (path.startsWith("/actuator") || path.startsWith("/health") ||
                                                path.startsWith("/swagger-ui") || path.startsWith("/api"))) {
                                    return MatchResult.notMatch();
                                }
                                return MatchResult.match();
                            }
                        })
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .authorizeExchange(exchanges -> exchanges
                        .anyExchange().permitAll()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
