package com.company.mtbp.customer.service;

import com.company.mtbp.customer.exception.BookingException;
import com.company.mtbp.customer.request.AudienceBookingRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@Slf4j
public class BookingService {
    private final WebClient webClient;

    public BookingService(WebClient webClient) {
        this.webClient = webClient;
    }

    @CircuitBreaker(name = "bookingService", fallbackMethod = "fallbackBooking")
    @Retry(name = "bookingService", fallbackMethod = "fallbackBooking")
    public Mono<String> bookTickets(AudienceBookingRequest request) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/bookings")
                        .queryParam("customerId", request.getCustomerId())
                        .queryParam("showId", request.getShowId())
                        .build()
                )
                .bodyValue(request)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        clientResponse -> Mono.error(new BookingException("Client error during booking"))
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        clientResponse -> Mono.error(new BookingException("Server error during booking"))
                )
                .bodyToMono(String.class)
                .retryWhen(
                        reactor.util.retry.Retry
                                .backoff(3, Duration.ofMillis(500))
                                .filter(ex -> ex instanceof RuntimeException)
                                .doBeforeRetry(retrySignal ->
                                        log.warn("Retrying booking attempt #{} due to error: {}",
                                                retrySignal.totalRetries() + 1,
                                                retrySignal.failure().getMessage()))
                                .onRetryExhaustedThrow((retrySpec, signal) ->
                                        new BookingException("Max retries exhausted", signal.failure()))
                );
    }

    Mono<String> fallbackBooking(AudienceBookingRequest request, Throwable ex) {
        return Mono.just("Booking service unavailable, please try again later");
    }

}
