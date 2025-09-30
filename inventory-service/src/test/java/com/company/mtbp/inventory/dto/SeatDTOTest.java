package com.company.mtbp.inventory.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SeatDTOTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        SeatDTO dto = new SeatDTO(1L, "R1", "REGULAR", true, 101L, "Big Theatre", 201L);

        assertEquals(1L, dto.getId());
        assertEquals("R1", dto.getSeatNumber());
        assertEquals("REGULAR", dto.getSeatType());
        assertTrue(dto.getAvailable());
        assertEquals(101L, dto.getTheatreId());
        assertEquals("Big Theatre", dto.getTheatreName());
        assertEquals(201L, dto.getShowId());
    }

    @Test
    void testSetters() {
        SeatDTO dto = new SeatDTO();
        dto.setId(2L);
        dto.setSeatNumber("P1");
        dto.setSeatType("PREMIUM");
        dto.setAvailable(false);
        dto.setTheatreId(102L);
        dto.setTheatreName("Mini Theatre");
        dto.setShowId(202L);

        assertEquals(2L, dto.getId());
        assertEquals("P1", dto.getSeatNumber());
        assertEquals("PREMIUM", dto.getSeatType());
        assertFalse(dto.getAvailable());
        assertEquals(102L, dto.getTheatreId());
        assertEquals("Mini Theatre", dto.getTheatreName());
        assertEquals(202L, dto.getShowId());
    }

    @Test
    void testBuilder() {
        SeatDTO dto = SeatDTO.builder()
                .id(3L)
                .seatNumber("V1")
                .seatType("VIP")
                .available(true)
                .theatreId(103L)
                .theatreName("Elite Theatre")
                .showId(203L)
                .build();

        assertEquals(3L, dto.getId());
        assertEquals("V1", dto.getSeatNumber());
        assertEquals("VIP", dto.getSeatType());
        assertTrue(dto.getAvailable());
        assertEquals(103L, dto.getTheatreId());
        assertEquals("Elite Theatre", dto.getTheatreName());
        assertEquals(203L, dto.getShowId());
    }

}
