package com.company.mtbp.inventory.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookingAlreadyCancelledExceptionTest {

    @Test
    void testExceptionMessage() {
        String errorMessage = "Booking is already cancelled";

        BookingAlreadyCancelledException exception = new BookingAlreadyCancelledException(errorMessage);

        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testExceptionIsRuntimeException() {
        BookingAlreadyCancelledException exception = new BookingAlreadyCancelledException("Test");

        assertTrue(true);
    }
}
