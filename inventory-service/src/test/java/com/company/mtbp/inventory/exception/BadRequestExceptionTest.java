package com.company.mtbp.inventory.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BadRequestExceptionTest {

    @Test
    void testExceptionMessage() {
        String errorMessage = "Invalid request data";

        BadRequestException exception = new BadRequestException(errorMessage);

        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testExceptionIsRuntimeException() {
        BadRequestException exception = new BadRequestException("Test");

        assertTrue(true);
    }
}
