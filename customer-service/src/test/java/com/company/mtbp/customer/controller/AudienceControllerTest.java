package com.company.mtbp.customer.controller;

import com.company.mtbp.customer.request.AudienceBookingRequest;
import com.company.mtbp.customer.request.PaymentRequest;
import com.company.mtbp.customer.service.BookingService;
import com.company.mtbp.customer.service.PaymentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

@WebFluxTest(controllers = AudienceController.class)
class AudienceControllerTest {

    @MockitoBean
    private BookingService bookingService;

    @MockitoBean
    private PaymentService paymentService;

    private AudienceController controller;

    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
            return http
                    // CSRF is disabled because this is a stateless API using JWT tokens
                    .csrf(csrf -> csrf.disable())
                    .authorizeExchange(auth -> auth.anyExchange().permitAll())
                    .build();
        }
    }

    @BeforeEach
    void setUp() {
        controller = new AudienceController(bookingService, paymentService);
    }

    @Test
    void testBookTicketsSuccess() {
        AudienceBookingRequest request = new AudienceBookingRequest();
        request.setCustomerId(1L);
        request.setShowId(1L);
        request.setSeatIds(List.of(3L, 4L));

        Mockito.when(bookingService.bookTickets(request))
                .thenReturn(Mono.just("Booking successful"));

        Mono<ResponseEntity<String>> responseMono = controller.bookTickets(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(response ->
                {
                    if (response.getStatusCode() != HttpStatus.OK) return false;
                    Assertions.assertNotNull(response.getBody());
                    return response.getBody().equals("Booking successful");
                })
                .verifyComplete();
    }

    @Test
    void testBookTicketsClientError() {
        AudienceBookingRequest request = new AudienceBookingRequest();
        request.setCustomerId(1L);
        request.setShowId(1L);
        request.setSeatIds(List.of(3L, 4L));

        Mockito.when(bookingService.bookTickets(request))
                .thenReturn(Mono.error(new RuntimeException("Client error during booking")));

        Mono<ResponseEntity<String>> responseMono = controller.bookTickets(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(response ->
                {
                    if (response.getStatusCode() != HttpStatus.BAD_REQUEST) return false;
                    Assertions.assertNotNull(response.getBody());
                    return response.getBody().equals("Client error during booking");
                })
                .verifyComplete();
    }

    @Test
    void testBookAndPaySuccess() {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setBookingId(1L);
        paymentRequest.setPaymentMethod("UPI");
        paymentRequest.setAmount(500.0);

        Mockito.when(paymentService.makePayment(paymentRequest))
                .thenReturn(Mono.just("Payment successful"));

        Mono<ResponseEntity<String>> responseMono = controller.bookAndPay(paymentRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(response ->
                {
                    if (response.getStatusCode() != HttpStatus.OK) return false;
                    Assertions.assertNotNull(response.getBody());
                    return response.getBody().equals("Booking and payment successful!");
                })
                .verifyComplete();
    }

    @Test
    void testBookAndPayError() {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setBookingId(1L);
        paymentRequest.setPaymentMethod("GPAY");
        paymentRequest.setAmount(500.0);

        Mockito.when(paymentService.makePayment(paymentRequest))
                .thenReturn(Mono.error(new RuntimeException("Payment failed")));

        Mono<ResponseEntity<String>> responseMono = controller.bookAndPay(paymentRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(response ->
                {
                    if (response.getStatusCode() != HttpStatus.INTERNAL_SERVER_ERROR) return false;
                    Assertions.assertNotNull(response.getBody());
                    return response.getBody().equals("Error during booking/payment: Payment failed");
                })
                .verifyComplete();
    }

    @Test
    void testBookTicketsServerError() {
        AudienceBookingRequest request = new AudienceBookingRequest();
        Mockito.when(bookingService.bookTickets(request))
                .thenReturn(Mono.error(new RuntimeException("Server error during booking")));

        StepVerifier.create(controller.bookTickets(request))
                .expectNextMatches(resp -> {
                    if (resp.getStatusCode() != HttpStatus.INTERNAL_SERVER_ERROR) return false;
                    Assertions.assertNotNull(resp.getBody());
                    return resp.getBody().equals("Server error during booking");
                })
                .verifyComplete();
    }

    @Test
    void testBookTicketsUnexpectedError() {
        AudienceBookingRequest request = new AudienceBookingRequest();
        Mockito.when(bookingService.bookTickets(request))
                .thenReturn(Mono.error(new RuntimeException("Database down")));

        StepVerifier.create(controller.bookTickets(request))
                .expectNextMatches(resp -> {
                    if (resp.getStatusCode() != HttpStatus.INTERNAL_SERVER_ERROR) return false;
                    Assertions.assertNotNull(resp.getBody());
                    return resp.getBody().equals("Unexpected error: Database down");
                })
                .verifyComplete();
    }

    @Test
    void testBulkBookTicketsSuccess() {
        Mockito.when(bookingService.bulkBookTickets(1L, 2L, 5))
                .thenReturn(Mono.just("Bulk booking successful"));

        StepVerifier.create(controller.bulkBookTickets(1L, 2L, 5))
                .expectNextMatches(resp -> {
                    if (resp.getStatusCode() != HttpStatus.OK) return false;
                    Assertions.assertNotNull(resp.getBody());
                    return resp.getBody().equals("Bulk booking successful");
                })
                .verifyComplete();
    }

    @Test
    void testBulkBookTicketsClientError() {
        Mockito.when(bookingService.bulkBookTickets(1L, 2L, 5))
                .thenReturn(Mono.error(new RuntimeException("Client error during bulk booking")));

        StepVerifier.create(controller.bulkBookTickets(1L, 2L, 5))
                .expectNextMatches(resp -> {
                    if (resp.getStatusCode() != HttpStatus.BAD_REQUEST) return false;
                    Assertions.assertNotNull(resp.getBody());
                    return resp.getBody().equals("Client error during bulk booking");
                })
                .verifyComplete();
    }

    @Test
    void testBulkBookTicketsServerError() {
        Mockito.when(bookingService.bulkBookTickets(1L, 2L, 5))
                .thenReturn(Mono.error(new RuntimeException("Server error during bulk booking")));

        StepVerifier.create(controller.bulkBookTickets(1L, 2L, 5))
                .expectNextMatches(resp -> {
                    if (resp.getStatusCode() != HttpStatus.INTERNAL_SERVER_ERROR) return false;
                    Assertions.assertNotNull(resp.getBody());
                    return resp.getBody().equals("Server error during bulk booking");
                })
                .verifyComplete();
    }

    @Test
    void testBulkBookTicketsUnexpectedError() {
        Mockito.when(bookingService.bulkBookTickets(1L, 2L, 5))
                .thenReturn(Mono.error(new RuntimeException("Database down")));

        StepVerifier.create(controller.bulkBookTickets(1L, 2L, 5))
                .expectNextMatches(resp -> {
                    if (resp.getStatusCode() != HttpStatus.INTERNAL_SERVER_ERROR) return false;
                    Assertions.assertNotNull(resp.getBody());
                    return resp.getBody().equals("Unexpected error: Database down");
                })
                .verifyComplete();
    }

    @Test
    void testCancelBookingSuccess() {
        Mockito.when(bookingService.cancelBooking(10L))
                .thenReturn(Mono.just("Booking cancelled"));

        StepVerifier.create(controller.cancelBooking(10L))
                .expectNextMatches(resp -> {
                    if (resp.getStatusCode() != HttpStatus.OK) return false;
                    Assertions.assertNotNull(resp.getBody());
                    return resp.getBody().equals("Booking cancelled");
                })
                .verifyComplete();
    }

    @Test
    void testCancelBookingClientError() {
        Mockito.when(bookingService.cancelBooking(10L))
                .thenReturn(Mono.error(new RuntimeException("Client error during cancel booking")));

        StepVerifier.create(controller.cancelBooking(10L))
                .expectNextMatches(resp -> {
                    if (resp.getStatusCode() != HttpStatus.BAD_REQUEST) return false;
                    Assertions.assertNotNull(resp.getBody());
                    return resp.getBody().equals("Client error during cancel booking");
                })
                .verifyComplete();
    }

    @Test
    void testCancelBookingServerError() {
        Mockito.when(bookingService.cancelBooking(10L))
                .thenReturn(Mono.error(new RuntimeException("Server error during cancel booking")));

        StepVerifier.create(controller.cancelBooking(10L))
                .expectNextMatches(resp -> {
                    if (resp.getStatusCode() != HttpStatus.INTERNAL_SERVER_ERROR) return false;
                    Assertions.assertNotNull(resp.getBody());
                    return resp.getBody().equals("Server error during cancel booking");
                })
                .verifyComplete();
    }

    @Test
    void testCancelBookingUnexpectedError() {
        Mockito.when(bookingService.cancelBooking(10L))
                .thenReturn(Mono.error(new RuntimeException("Unknown error")));

        StepVerifier.create(controller.cancelBooking(10L))
                .expectNextMatches(resp -> {
                    if (resp.getStatusCode() != HttpStatus.INTERNAL_SERVER_ERROR) return false;
                    Assertions.assertNotNull(resp.getBody());
                    return resp.getBody().equals("Unexpected error: Unknown error");
                })
                .verifyComplete();
    }

    @Test
    void testBrowseShowsSuccess() {
        Mockito.when(bookingService.browseShows("Pirates", "Hyderabad", "2025-09-30"))
                .thenReturn(Mono.just("Shows found"));

        StepVerifier.create(controller.browseShows("Pirates", "Hyderabad", "2025-09-30"))
                .expectNextMatches(resp -> {
                    if (resp.getStatusCode() != HttpStatus.OK) return false;
                    Assertions.assertNotNull(resp.getBody());
                    return resp.getBody().equals("Shows found");
                })
                .verifyComplete();
    }

    @Test
    void testBrowseShowsClientError() {
        Mockito.when(bookingService.browseShows("Pirates", "Hyderabad", "2025-09-30"))
                .thenReturn(Mono.error(new RuntimeException("Client error while browsing shows")));

        StepVerifier.create(controller.browseShows("Pirates", "Hyderabad", "2025-09-30"))
                .expectNextMatches(resp -> {
                    if (resp.getStatusCode() != HttpStatus.BAD_REQUEST) return false;
                    Assertions.assertNotNull(resp.getBody());
                    return resp.getBody().equals("Client error while browsing shows");
                })
                .verifyComplete();
    }

    @Test
    void testBrowseShowsServerError() {
        Mockito.when(bookingService.browseShows("Pirates", "Hyderabad", "2025-09-30"))
                .thenReturn(Mono.error(new RuntimeException("Server error while browsing shows")));

        StepVerifier.create(controller.browseShows("Pirates", "Hyderabad", "2025-09-30"))
                .expectNextMatches(resp -> {
                    if (resp.getStatusCode() != HttpStatus.INTERNAL_SERVER_ERROR) return false;
                    Assertions.assertNotNull(resp.getBody());
                    return resp.getBody().equals("Server error while browsing shows");
                })
                .verifyComplete();
    }

    @Test
    void testBrowseShowsUnexpectedError() {
        Mockito.when(bookingService.browseShows("Pirates", "Hyderabad", "2025-09-30"))
                .thenReturn(Mono.error(new RuntimeException("Timeout")));

        StepVerifier.create(controller.browseShows("Pirates", "Hyderabad", "2025-09-30"))
                .expectNextMatches(resp -> {
                    if (resp.getStatusCode() != HttpStatus.INTERNAL_SERVER_ERROR) return false;
                    Assertions.assertNotNull(resp.getBody());
                    return resp.getBody().equals("Unexpected error: Timeout");
                })
                .verifyComplete();
    }

    @Test
    void testGetOffersSuccess() {
        Mockito.when(bookingService.getOffers(1L, 3L))
                .thenReturn(Mono.just("Offers found"));

        StepVerifier.create(controller.getOffers(1L, 3L))
                .expectNextMatches(resp -> {
                    if (resp.getStatusCode() != HttpStatus.OK) return false;
                    Assertions.assertNotNull(resp.getBody());
                    return resp.getBody().equals("Offers found");
                })
                .verifyComplete();
    }

    @Test
    void testGetOffersClientError() {
        Mockito.when(bookingService.getOffers(1L, 3L))
                .thenReturn(Mono.error(new RuntimeException("Client error fetching offers")));

        StepVerifier.create(controller.getOffers(1L, 3L))
                .expectNextMatches(resp -> {
                    if (resp.getStatusCode() != HttpStatus.BAD_REQUEST) return false;
                    Assertions.assertNotNull(resp.getBody());
                    return resp.getBody().equals("Client error fetching offers");
                })
                .verifyComplete();
    }

    @Test
    void testGetOffersServerError() {
        Mockito.when(bookingService.getOffers(1L, 3L))
                .thenReturn(Mono.error(new RuntimeException("Server error fetching offers")));

        StepVerifier.create(controller.getOffers(1L, 3L))
                .expectNextMatches(resp -> {
                    if (resp.getStatusCode() != HttpStatus.INTERNAL_SERVER_ERROR) return false;
                    Assertions.assertNotNull(resp.getBody());
                    return resp.getBody().equals("Server error fetching offers");
                })
                .verifyComplete();
    }

    @Test
    void testGetOffersUnexpectedError() {
        Mockito.when(bookingService.getOffers(1L, 3L))
                .thenReturn(Mono.error(new RuntimeException("Service down")));

        StepVerifier.create(controller.getOffers(1L, 3L))
                .expectNextMatches(resp -> {
                    if (resp.getStatusCode() != HttpStatus.INTERNAL_SERVER_ERROR) return false;
                    Assertions.assertNotNull(resp.getBody());
                    return resp.getBody().equals("Unexpected error: Service down");
                })
                .verifyComplete();
    }
}
