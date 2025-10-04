package com.company.mtbp.partner.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.net.URI;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(TheatrePartnerException.class)
    public ResponseEntity<ProblemDetail> handleTheatrePartnerException(TheatrePartnerException ex) {
        String details = ex.getMessage();
        log.error("Logging Theatre Partner ServiceException : {}", details);
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        pd.setType(URI.create("urn:problem-type:theatre-partner-service-problem"));
        pd.setDetail("Theatre Partners Error Details : " + details);
        pd.setTitle("Theatre Partners Service Error");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pd);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<String> handleValidationException(WebExchangeBindException ex) {
        String errorMessage = ex.getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .reduce("", (msg1, msg2) -> msg1 + "; " + msg2);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGeneralException(Exception ex) {
        String details = ex.getMessage();
        log.error("Logging TheatrePartnerServiceException : {}", details);
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.SERVICE_UNAVAILABLE);
        pd.setType(URI.create("urn:problem-type:theatre-partner-service-problem"));
        pd.setDetail("Theatre Partner Error Details : " + details);
        pd.setTitle("Theatre Partner Service Error");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pd);
    }
}