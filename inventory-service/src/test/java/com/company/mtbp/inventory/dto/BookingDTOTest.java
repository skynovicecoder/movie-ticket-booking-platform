package com.company.mtbp.inventory.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookingDTOTest {

    @Test
    void testAllFieldsUsingConstructorAndGetters() {
        LocalDateTime now = LocalDateTime.now();
        BookingDTO dto = new BookingDTO(
                1L,
                100L,
                "John Doe",
                200L,
                now,
                now,
                500.0,
                "BOOKED",
                List.of(10L, 20L)
        );

        assertEquals(1L, dto.getId());
        assertEquals(100L, dto.getCustomerId());
        assertEquals("John Doe", dto.getCustomerName());
        assertEquals(200L, dto.getShowId());
        assertEquals(now, dto.getShowDateTime());
        assertEquals(now, dto.getBookingTime());
        assertEquals(500.0, dto.getTotalAmount());
        assertEquals("BOOKED", dto.getStatus());
        assertEquals(List.of(10L, 20L), dto.getBookingDetailIds());
    }

    @Test
    void testBuilder() {
        LocalDateTime now = LocalDateTime.now();
        BookingDTO dto = BookingDTO.builder()
                .id(2L)
                .customerId(101L)
                .customerName("Jane Doe")
                .showId(201L)
                .showDateTime(now)
                .bookingTime(now)
                .totalAmount(750.0)
                .status("CANCELLED")
                .bookingDetailIds(List.of(30L, 40L))
                .build();

        assertEquals(2L, dto.getId());
        assertEquals(101L, dto.getCustomerId());
        assertEquals("Jane Doe", dto.getCustomerName());
        assertEquals(201L, dto.getShowId());
        assertEquals(now, dto.getShowDateTime());
        assertEquals(now, dto.getBookingTime());
        assertEquals(750.0, dto.getTotalAmount());
        assertEquals("CANCELLED", dto.getStatus());
        assertEquals(List.of(30L, 40L), dto.getBookingDetailIds());
    }

}
