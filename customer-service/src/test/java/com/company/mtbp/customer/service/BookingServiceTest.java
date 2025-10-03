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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private WebClient webClient;

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
}
