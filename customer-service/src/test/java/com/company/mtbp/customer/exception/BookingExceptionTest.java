package com.company.mtbp.customer.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BookingExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String message = "Booking service unavailable";
        BookingException exception = new BookingException(message);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        String message = "Booking service unavailable";
        Throwable cause = new RuntimeException("Underlying cause");
        BookingException exception = new BookingException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
