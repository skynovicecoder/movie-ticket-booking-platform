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
    void testEqualsAndHashCode() {
        PaymentRequest req1 = new PaymentRequest(1L, 100.0, "CARD");
        PaymentRequest req2 = new PaymentRequest(1L, 100.0, "CARD");
        PaymentRequest req3 = new PaymentRequest(2L, 200.0, "UPI");

        assertEquals(req1, req2);
        assertEquals(req1.hashCode(), req2.hashCode());
        assertNotEquals(req1, req3);
        assertNotEquals(req1.hashCode(), req3.hashCode());
    }

    @Test
    void testToString() {
        PaymentRequest request = new PaymentRequest(1L, 100.0, "CARD");
        String str = request.toString();
        assertTrue(str.contains("bookingId=1"));
        assertTrue(str.contains("amount=100.0"));
        assertTrue(str.contains("paymentMethod=CARD"));
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
}
