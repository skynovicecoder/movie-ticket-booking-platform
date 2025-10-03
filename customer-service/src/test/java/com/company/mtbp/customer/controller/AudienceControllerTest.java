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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
