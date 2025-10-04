package com.company.mtbp.customer.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AudienceBookingRequestTest {

    private final Validator validator;

    public AudienceBookingRequestTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testGettersAndSetters() {
        AudienceBookingRequest request = new AudienceBookingRequest();

        request.setCustomerId(101L);
        request.setShowId(202L);
        request.setSeatIds(Arrays.asList(1L, 2L, 3L));

        assertEquals(101L, request.getCustomerId());
        assertEquals(202L, request.getShowId());
        assertEquals(Arrays.asList(1L, 2L, 3L), request.getSeatIds());
    }

    @Test
    void testAllArgsConstructor() {
        List<Long> seats = Arrays.asList(4L, 5L);
        AudienceBookingRequest request = new AudienceBookingRequest(1L, 2L, seats);

        assertEquals(1L, request.getCustomerId());
        assertEquals(2L, request.getShowId());
        assertEquals(seats, request.getSeatIds());
    }

    @Test
    void testEqualsAndHashCode() {
        AudienceBookingRequest req1 = new AudienceBookingRequest(1L, 2L, Arrays.asList(1L, 2L));
        AudienceBookingRequest req2 = new AudienceBookingRequest(1L, 2L, Arrays.asList(1L, 2L));
        AudienceBookingRequest req3 = new AudienceBookingRequest(3L, 4L, Arrays.asList(3L, 4L));

        assertEquals(req1, req2);
        assertEquals(req1.hashCode(), req2.hashCode());
        assertNotEquals(req1, req3);
        assertNotEquals(req1.hashCode(), req3.hashCode());
    }

    @Test
    void testValidationConstraints() {
        AudienceBookingRequest invalidRequest = new AudienceBookingRequest(null, null, null);

        Set<ConstraintViolation<AudienceBookingRequest>> violations = validator.validate(invalidRequest);

        assertEquals(3, violations.size());

        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Customer ID cannot be null")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Show ID cannot be null")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Seat IDs cannot be empty")));
    }

    @Test
    void testValidRequest() {
        AudienceBookingRequest validRequest = new AudienceBookingRequest(1L, 2L, Arrays.asList(1L, 2L));
        Set<ConstraintViolation<AudienceBookingRequest>> violations = validator.validate(validRequest);
        assertTrue(violations.isEmpty());
    }

    @Test
    void noArgsConstructorAndToString_ShouldBeCovered() {
        AudienceBookingRequest request = new AudienceBookingRequest();
        request.setCustomerId(10L);
        request.setShowId(20L);
        request.setSeatIds(Arrays.asList(1L, 2L));

        String str = request.toString();
        assertNotNull(str);
        assertTrue(str.contains("customerId=10"));
        assertTrue(str.contains("showId=20"));
        assertTrue(str.contains("seatIds=[1, 2]"));
    }
}
