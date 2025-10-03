package com.company.mtbp.customer.service;

import com.company.mtbp.customer.exception.PaymentException;
import com.company.mtbp.customer.request.PaymentRequest;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.retry.Retry;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class PaymentService {
    private final WebClient webClient;
    CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("paymentService");
    private final Retry paymentRetry;

    public PaymentService(WebClient webClient, Retry paymentRetry) {
        this.webClient = webClient;
        this.paymentRetry = paymentRetry;
    }

    @PostConstruct
    public void init() {
        circuitBreaker.getEventPublisher()
                .onStateTransition(event ->
                        log.info("CircuitBreaker '{}' state transition: {} -> {}",
                                event.getCircuitBreakerName(),
                                event.getStateTransition().getFromState(),
                                event.getStateTransition().getToState()))
                .onCallNotPermitted(event ->
                        log.warn("CircuitBreaker '{}' blocked a call!", event.getCircuitBreakerName()))
                .onError(event ->
                        log.error("CircuitBreaker '{}' recorded an error: {}",
                                event.getCircuitBreakerName(),
                                event.getThrowable().getMessage()));

        paymentRetry.getEventPublisher()
                .onRetry(event ->
                        log.warn("Resilience4j payment retry attempt #{} due to {}",
                                event.getNumberOfRetryAttempts(),
                                event.getLastThrowable().getMessage()));
    }

    public Mono<String> makePayment(PaymentRequest request) {
        return webClient.post()
                .uri("/api/v1/payments")
                .bodyValue(request)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        clientResponse -> Mono.error(new PaymentException("Client error during booking"))
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        clientResponse -> Mono.error(new PaymentException("Server error during booking"))
                )
                .bodyToMono(String.class)
                .transformDeferred(CircuitBreakerOperator.of(circuitBreaker))
                .transformDeferred(RetryOperator.of(paymentRetry));
    }

    private Mono<String> paymentFallback(PaymentRequest request, Throwable ex) {
        return Mono.just("Payment service unavailable. Please try again later.");
    }
}
