package com.company.mtbp.inventory.dto;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SeatSelectionRequestTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        SeatSelectionRequest request = new SeatSelectionRequest(List.of(1L, 2L, 3L));

        assertNotNull(request.getSeatIds());
        assertEquals(3, request.getSeatIds().size());
        assertEquals(List.of(1L, 2L, 3L), request.getSeatIds());
    }

    @Test
    void testSetters() {
        SeatSelectionRequest request = new SeatSelectionRequest();
        request.setSeatIds(List.of(4L, 5L));

        assertNotNull(request.getSeatIds());
        assertEquals(2, request.getSeatIds().size());
        assertEquals(List.of(4L, 5L), request.getSeatIds());
    }

    @Test
    void testToString() {
        SeatSelectionRequest request = new SeatSelectionRequest(List.of(6L, 7L));
        String str = request.toString();

        assertNotNull(str);
        assertTrue(str.contains("6"));
        assertTrue(str.contains("7"));
    }

    @Test
    void testEqualsAndHashCode() {
        SeatSelectionRequest r1 = new SeatSelectionRequest(List.of(8L, 9L));
        SeatSelectionRequest r2 = new SeatSelectionRequest(List.of(8L, 9L));

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }
}
