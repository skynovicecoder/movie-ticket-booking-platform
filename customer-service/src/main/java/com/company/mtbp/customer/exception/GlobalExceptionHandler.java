package com.company.mtbp.customer.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.net.URI;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BookingException.class)
    public Mono<ResponseEntity<ProblemDetail>> handleBookingException(BookingException ex) {
        String details = ex.getMessage();
        log.error("Logging BookingException for AudienceServiceException : {}", details);
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.SERVICE_UNAVAILABLE);
        pd.setType(URI.create("urn:problem-type:audience-booking-service-problem"));
        pd.setDetail("Audience Booking Exception Error Details : " + details);
        pd.setTitle("Audience Booking Exception Service Error");

        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pd));
    }

    @ExceptionHandler(PaymentException.class)
    public Mono<ResponseEntity<ProblemDetail>> handlePaymentException(PaymentException ex) {
        String details = ex.getMessage();
        log.error("Logging Payment AudienceServiceException : {}", details);
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.PAYMENT_REQUIRED);
        pd.setType(URI.create("urn:problem-type:audience-payment-service-problem"));
        pd.setDetail("Audience Payment Error Details : " + details);
        pd.setTitle("Audience Payment Service Error");

        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pd));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<String>> handleValidationException(WebExchangeBindException ex) {
        String errorMessage = ex.getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .reduce("", (msg1, msg2) -> msg1 + "; " + msg2);
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ProblemDetail>> handleGeneralException(Exception ex) {
        String details = ex.getMessage();
        log.error("Logging AudienceServiceException : {}", details);
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.SERVICE_UNAVAILABLE);
        pd.setType(URI.create("urn:problem-type:audience-service-problem"));
        pd.setDetail("Audience Error Details : " + details);
        pd.setTitle("Audience Service Error");

        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pd));
    }
}
