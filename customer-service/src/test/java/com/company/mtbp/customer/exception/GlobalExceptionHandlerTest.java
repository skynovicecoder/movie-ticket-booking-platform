package com.company.mtbp.customer.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleBookingException() {
        BookingException ex = new BookingException("Booking failed");

        Mono<ResponseEntity<ProblemDetail>> resultMono = handler.handleBookingException(ex);

        StepVerifier.create(resultMono)
                .assertNext(response -> {
                    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
                    ProblemDetail pd = response.getBody();
                    assertNotNull(pd);
                    assertEquals("Audience Booking Exception Service Error", pd.getTitle());
                    assertNotNull(pd.getDetail());
                    assertTrue(pd.getDetail().contains("Booking failed"));
                    assertEquals("urn:problem-type:audience-booking-service-problem", pd.getType().toString());
                })
                .verifyComplete();
    }

    @Test
    void testHandlePaymentException() {
        PaymentException ex = new PaymentException("Payment failed");

        Mono<ResponseEntity<ProblemDetail>> resultMono = handler.handlePaymentException(ex);

        StepVerifier.create(resultMono)
                .assertNext(response -> {
                    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
                    ProblemDetail pd = response.getBody();
                    assertNotNull(pd);
                    assertEquals("Audience Payment Service Error", pd.getTitle());
                    assertNotNull(pd.getDetail());
                    assertTrue(pd.getDetail().contains("Payment failed"));
                    assertEquals("urn:problem-type:audience-payment-service-problem", pd.getType().toString());
                })
                .verifyComplete();
    }

    @Test
    void testHandleValidationException() {
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError fieldError1 = new FieldError("object", "field1", "must not be null");
        FieldError fieldError2 = new FieldError("object", "field2", "must be positive");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));

        WebExchangeBindException ex = new WebExchangeBindException(null, bindingResult);

        Mono<ResponseEntity<String>> resultMono = handler.handleValidationException(ex);

        StepVerifier.create(resultMono)
                .assertNext(response -> {
                    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                    String body = response.getBody();
                    assertNotNull(body);
                    assertTrue(body.contains("field1: must not be null"));
                    assertTrue(body.contains("field2: must be positive"));
                })
                .verifyComplete();
    }

    @Test
    void testHandleGeneralException() {
        Exception ex = new Exception("Something went wrong");

        Mono<ResponseEntity<ProblemDetail>> resultMono = handler.handleGeneralException(ex);

        StepVerifier.create(resultMono)
                .assertNext(response -> {
                    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
                    ProblemDetail pd = response.getBody();
                    assertNotNull(pd);
                    assertEquals("Audience Service Error", pd.getTitle());
                    assertNotNull(pd.getDetail());
                    assertTrue(pd.getDetail().contains("Something went wrong"));
                    assertEquals("urn:problem-type:audience-service-problem", pd.getType().toString());
                })
                .verifyComplete();
    }
}
