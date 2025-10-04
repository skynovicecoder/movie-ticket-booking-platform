package com.company.mtbp.partner.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(
                                // CSRF is disabled because this is a stateless API using JWT tokens
                                request -> HttpMethod.GET.matches(request.getMethod()) ||
                                        (request.getRequestURI().startsWith("/actuator") ||
                                                request.getRequestURI().startsWith("/health") ||
                                                request.getRequestURI().startsWith("/swagger-ui") ||
                                                request.getRequestURI().startsWith("/api"))
                        )
                )
                .cors(cors -> {
                })
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
