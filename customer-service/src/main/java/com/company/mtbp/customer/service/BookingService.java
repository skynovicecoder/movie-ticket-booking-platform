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

    @CircuitBreaker(name = "bookingService", fallbackMethod = "fallbackBulkBooking")
    @Retry(name = "bookingService", fallbackMethod = "fallbackBulkBooking")
    public Mono<String> bulkBookTickets(Long customerId, Long showId, int numberOfTicketsReq) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/bookings/bulk")
                        .queryParam("customerId", customerId)
                        .queryParam("showId", showId)
                        .queryParam("numberOfTicketsReq", numberOfTicketsReq)
                        .build()
                )
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        clientResponse -> Mono.error(new BookingException("Client error during bulk booking"))
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        clientResponse -> Mono.error(new BookingException("Server error during bulk booking"))
                )
                .bodyToMono(String.class)
                .retryWhen(
                        reactor.util.retry.Retry
                                .backoff(3, Duration.ofMillis(500))
                                .filter(ex -> ex instanceof RuntimeException)
                                .doBeforeRetry(retrySignal ->
                                        log.warn("Retrying bulk booking attempt #{} due to error: {}",
                                                retrySignal.totalRetries() + 1,
                                                retrySignal.failure().getMessage()))
                                .onRetryExhaustedThrow((retrySpec, signal) ->
                                        new BookingException("Max retries exhausted", signal.failure()))
                );
    }

    Mono<String> fallbackBulkBooking(Long customerId, Long showId, int numberOfTicketsReq, Throwable ex) {
        return Mono.just("Bulk booking service unavailable, please try again later");
    }

    @CircuitBreaker(name = "bookingService", fallbackMethod = "fallbackCancelBooking")
    @Retry(name = "bookingService", fallbackMethod = "fallbackCancelBooking")
    public Mono<String> cancelBooking(Long bookingId) {
        return webClient.put()
                .uri("/api/v1/bookings/{bookingId}/cancel", bookingId)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        clientResponse -> Mono.error(new BookingException("Client error during cancel booking"))
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        clientResponse -> Mono.error(new BookingException("Server error during cancel booking"))
                )
                .bodyToMono(String.class)
                .retryWhen(
                        reactor.util.retry.Retry
                                .backoff(3, Duration.ofMillis(500))
                                .filter(ex -> ex instanceof RuntimeException)
                                .doBeforeRetry(retrySignal ->
                                        log.warn("Retrying cancel booking attempt #{} due to error: {}",
                                                retrySignal.totalRetries() + 1,
                                                retrySignal.failure().getMessage()))
                                .onRetryExhaustedThrow((retrySpec, signal) ->
                                        new BookingException("Max retries exhausted", signal.failure()))
                );
    }

    Mono<String> fallbackCancelBooking(Long bookingId, Throwable ex) {
        return Mono.just("Cancel booking service unavailable, please try again later");
    }

    @CircuitBreaker(name = "showService", fallbackMethod = "fallbackBrowseShows")
    @Retry(name = "showService", fallbackMethod = "fallbackBrowseShows")
    public Mono<String> browseShows(String movieTitle, String cityName, String date) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/shows/browse")
                        .queryParam("movieTitle", movieTitle)
                        .queryParam("cityName", cityName)
                        .queryParam("date", date)
                        .build()
                )
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        clientResponse -> Mono.error(new BookingException("Client error during browse shows"))
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        clientResponse -> Mono.error(new BookingException("Server error during browse shows"))
                )
                .bodyToMono(String.class)
                .retryWhen(
                        reactor.util.retry.Retry
                                .backoff(3, Duration.ofMillis(500))
                                .filter(ex -> ex instanceof RuntimeException)
                                .doBeforeRetry(retrySignal ->
                                        log.warn("Retrying browse shows attempt #{} due to error: {}",
                                                retrySignal.totalRetries() + 1,
                                                retrySignal.failure().getMessage()))
                                .onRetryExhaustedThrow((retrySpec, signal) ->
                                        new BookingException("Max retries exhausted", signal.failure()))
                );
    }

    Mono<String> fallbackBrowseShows(String movieTitle, String cityName, String date, Throwable ex) {
        return Mono.just("Browse shows service unavailable, please try again later");
    }

    @CircuitBreaker(name = "discountService", fallbackMethod = "fallbackGetOffers")
    @Retry(name = "discountService", fallbackMethod = "fallbackGetOffers")
    public Mono<String> getOffers(Long cityId, Long theatreId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/discounts/offers")
                        .queryParam("cityId", cityId)
                        .queryParam("theatreId", theatreId)
                        .build()
                )
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        clientResponse -> Mono.error(new BookingException("Client error while fetching offers"))
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        clientResponse -> Mono.error(new BookingException("Server error while fetching offers"))
                )
                .bodyToMono(String.class)
                .retryWhen(
                        reactor.util.retry.Retry
                                .backoff(3, Duration.ofMillis(500))
                                .filter(ex -> ex instanceof RuntimeException)
                                .doBeforeRetry(retrySignal ->
                                        log.warn("Retrying getOffers attempt #{} due to error: {}",
                                                retrySignal.totalRetries() + 1,
                                                retrySignal.failure().getMessage()))
                                .onRetryExhaustedThrow((retrySpec, signal) ->
                                        new BookingException("Max retries exhausted", signal.failure()))
                );
    }

    Mono<String> fallbackGetOffers(Long cityId, Long theatreId, Throwable ex) {
        return Mono.just("Discount offers service unavailable, please try again later");
    }

}
