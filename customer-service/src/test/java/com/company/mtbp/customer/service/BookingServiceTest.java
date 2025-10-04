package com.company.mtbp.customer.service;

import com.company.mtbp.customer.exception.BookingException;
import com.company.mtbp.customer.request.AudienceBookingRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec uriSpec;

    @Mock
    private WebClient.RequestBodyUriSpec bodyUriSpec;

    @Mock
    private WebClient.RequestBodySpec bodySpec;

    @Mock
    private WebClient.RequestHeadersSpec headersSpec;

    @Mock
    private ResponseSpec responseSpec;

    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        bookingService = new BookingService(webClient);
    }

    private AudienceBookingRequest sampleRequest() {
        return new AudienceBookingRequest(1L, 100L, List.of(1L, 2L, 3L));
    }

    private void mockSuccessfulBooking(String responseBody) {
        when(webClient.post()).thenReturn(bodyUriSpec);
        when(bodyUriSpec.uri(any(java.util.function.Function.class))).thenReturn(bodySpec);
        when(bodySpec.bodyValue(any())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(responseBody));
    }

    private void mockBookingError(Throwable throwable) {
        when(webClient.post()).thenReturn(bodyUriSpec);
        when(bodyUriSpec.uri(any(java.util.function.Function.class))).thenReturn(bodySpec);
        when(bodySpec.bodyValue(any())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.error(throwable));
    }

    @Test
    void bookTickets_Success_ReturnsConfirmation() {
        mockSuccessfulBooking("Booking Confirmed");

        StepVerifier.create(bookingService.bookTickets(sampleRequest()))
                .expectNext("Booking Confirmed")
                .verifyComplete();
    }

    @Test
    void bookTickets_ClientError_ThrowsBookingException() {
        mockBookingError(new BookingException("Client error during booking"));

        StepVerifier.create(bookingService.bookTickets(sampleRequest()))
                .expectErrorSatisfies(ex -> {
                    assertThat(ex).isInstanceOf(BookingException.class)
                            .hasMessageContaining("Max retries exhausted")
                            .hasCauseInstanceOf(BookingException.class)
                            .extracting(Throwable::getCause)
                            .satisfies(cause -> assertThat(cause).hasMessageContaining("Client error"));
                })
                .verify();
    }

    @Test
    void bookTickets_ServerError_RetriesThenThrows() {
        mockBookingError(new RuntimeException("Server error"));

        StepVerifier.create(bookingService.bookTickets(sampleRequest()))
                .expectErrorSatisfies(ex -> {
                    assertThat(ex).isInstanceOf(BookingException.class)
                            .hasMessageContaining("Max retries exhausted");
                })
                .verify();
    }

    @Test
    void fallbackBooking_ReturnsFallbackMessage() {
        String fallback = bookingService
                .fallbackBooking(sampleRequest(), new RuntimeException("Down"))
                .block();

        assertThat(fallback).isEqualTo("Booking service unavailable, please try again later");
    }

    @Test
    void bookTickets_ServerError_TriggersFallback() {
        StepVerifier.create(bookingService.fallbackBooking(sampleRequest(), new RuntimeException("Server error")))
                .expectNext("Booking service unavailable, please try again later")
                .verifyComplete();
    }

    @Test
    void bookTickets_ClientError_RetriesExhausted_ThrowsBookingException() {
        mockBookingError(new RuntimeException("Client error"));

        StepVerifier.create(bookingService.bookTickets(sampleRequest()))
                .expectErrorSatisfies(ex -> {
                    assertThat(ex).isInstanceOf(BookingException.class)
                            .hasMessageContaining("Max retries exhausted")
                            .hasCauseInstanceOf(RuntimeException.class)
                            .extracting(Throwable::getCause)
                            .satisfies(cause -> assertThat(cause).hasMessageContaining("Client error"));
                })
                .verify();
    }

    private void mockBulkBookingSuccess(String response) {
        when(webClient.post()).thenReturn(bodyUriSpec);
        when(bodyUriSpec.uri(any(java.util.function.Function.class))).thenReturn(bodySpec);
        when(bodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(response));
    }

    private void mockBulkBookingError(Throwable t) {
        when(webClient.post()).thenReturn(bodyUriSpec);
        when(bodyUriSpec.uri(any(java.util.function.Function.class))).thenReturn(bodySpec);
        when(bodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.error(t));
    }

    @Test
    void bulkBookTickets_Success() {
        mockBulkBookingSuccess("Bulk booked");

        StepVerifier.create(bookingService.bulkBookTickets(1L, 2L, 5))
                .expectNext("Bulk booked")
                .verifyComplete();
    }

    @Test
    void bulkBookTickets_Fallback() {
        StepVerifier.create(bookingService.fallbackBulkBooking(1L, 2L, 5, new RuntimeException("Error")))
                .expectNext("Bulk booking service unavailable, please try again later")
                .verifyComplete();
    }

    @Test
    void bulkBookTickets_Error_ThrowsBookingException() {
        mockBulkBookingError(new RuntimeException("Server error"));

        StepVerifier.create(bookingService.bulkBookTickets(1L, 2L, 5))
                .expectErrorSatisfies(ex -> assertThat(ex).isInstanceOf(BookingException.class)
                        .hasMessageContaining("Max retries exhausted"))
                .verify();
    }

    private void mockCancelBookingSuccess(String response) {
        when(webClient.put()).thenReturn(bodyUriSpec);
        when(bodyUriSpec.uri(any(String.class), any(Object.class))).thenReturn(bodySpec);
        when(bodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(response));
    }

    @Test
    void cancelBooking_Success() {
        mockCancelBookingSuccess("Cancelled");

        StepVerifier.create(bookingService.cancelBooking(10L))
                .expectNext("Cancelled")
                .verifyComplete();
    }

    @Test
    void cancelBooking_Fallback() {
        StepVerifier.create(bookingService.fallbackCancelBooking(10L, new RuntimeException("Error")))
                .expectNext("Cancel booking service unavailable, please try again later")
                .verifyComplete();
    }

    private void mockBrowseShowsSuccess(String response) {
        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(any(java.util.function.Function.class))).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(response));
    }

    @Test
    void browseShows_Success() {
        mockBrowseShowsSuccess("Shows list");

        StepVerifier.create(bookingService.browseShows("Pirates", "Hyderabad", "2025-09-30"))
                .expectNext("Shows list")
                .verifyComplete();
    }

    @Test
    void browseShows_Fallback() {
        StepVerifier.create(bookingService.fallbackBrowseShows("Pirates", "Hyderabad", "2025-09-30", new RuntimeException("Error")))
                .expectNext("Browse shows service unavailable, please try again later")
                .verifyComplete();
    }

    private void mockGetOffersSuccess(String response) {
        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(any(Function.class))).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(response));
    }

    @Test
    void getOffers_Success() {
        mockGetOffersSuccess("Offers list");

        StepVerifier.create(bookingService.getOffers(1L, 3L))
                .expectNext("Offers list")
                .verifyComplete();
    }

    @Test
    void getOffers_Fallback() {
        StepVerifier.create(bookingService.fallbackGetOffers(1L, 3L, new RuntimeException("Error")))
                .expectNext("Discount offers service unavailable, please try again later")
                .verifyComplete();
    }

}
