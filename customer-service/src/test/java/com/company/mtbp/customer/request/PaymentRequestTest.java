package com.company.mtbp.customer.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testGettersAndSetters() {
        PaymentRequest request = new PaymentRequest();
        request.setBookingId(100L);
        request.setAmount(2500.0);
        request.setPaymentMethod("CARD");

        assertEquals(100L, request.getBookingId());
        assertEquals(2500.0, request.getAmount());
        assertEquals("CARD", request.getPaymentMethod());
    }

    @Test
    void testAllArgsConstructor() {
        PaymentRequest request = new PaymentRequest(101L, 500.0, "UPI");
        assertEquals(101L, request.getBookingId());
        assertEquals(500.0, request.getAmount());
        assertEquals("UPI", request.getPaymentMethod());
    }

    @Test
    void testValidation_notNullFields() {
        PaymentRequest request = new PaymentRequest(null, null, null);

        Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(request);

        assertEquals(3, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Booking ID cannot be null")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Amount cannot be null")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Payment Method cannot be null")));
    }

    @Test
    void noArgsConstructor_ShouldBeCovered() {
        PaymentRequest request = new PaymentRequest();
        request.setBookingId(10L);
        request.setAmount(100.0);
        request.setPaymentMethod("UPI");

        assertNotNull(request);
    }
}
