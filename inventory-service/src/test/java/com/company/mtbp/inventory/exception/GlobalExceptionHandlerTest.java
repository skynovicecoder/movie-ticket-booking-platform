package com.company.mtbp.inventory.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleBadRequestException_returnsBadRequestStatus() {
        BadRequestException ex = new BadRequestException("Invalid request");

        ResponseEntity<String> response = handler.handleBadRequestException(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid request", response.getBody());
    }

    @Test
    void handleResourceNotFound_returnsNotFoundStatus() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Resource not found");

        ResponseEntity<String> response = handler.handleResourceNotFound(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Resource not found", response.getBody());
    }

    @Test
    void handleBookingAlreadyCancelledException_returnsConflictStatus() {
        BookingAlreadyCancelledException ex = new BookingAlreadyCancelledException("Booking already cancelled");

        ResponseEntity<String> response = handler.handleBookingAlreadyCancelledException(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Booking already cancelled", response.getBody());
    }

    @Test
    void handleGeneralException_returnsInternalServerErrorStatus() {
        Exception ex = new Exception("Something went wrong");

        ResponseEntity<ProblemDetail> response = handler.handleGeneralException(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Inventory Error Details : Something went wrong", response.getBody().getDetail());
    }
}
