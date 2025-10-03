package com.company.mtbp.customer.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PaymentExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String message = "Payment required for this operation";
        PaymentException exception = new PaymentException(message);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause()); // cause should be null
    }
}
