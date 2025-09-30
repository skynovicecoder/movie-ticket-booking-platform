package com.company.mtbp.inventory.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TheatreDTOTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        TheatreDTO theatreDTO = new TheatreDTO(
                1L,
                "PVR Cinema",
                "MG Road, Bangalore",
                200,
                10L,
                "Bangalore"
        );

        assertEquals(1L, theatreDTO.getId());
        assertEquals("PVR Cinema", theatreDTO.getName());
        assertEquals("MG Road, Bangalore", theatreDTO.getAddress());
        assertEquals(200, theatreDTO.getTotalSeats());
        assertEquals(10L, theatreDTO.getCityId());
        assertEquals("Bangalore", theatreDTO.getCityName());
    }

    @Test
    void testSetters() {
        TheatreDTO theatreDTO = new TheatreDTO();
        theatreDTO.setId(2L);
        theatreDTO.setName("INOX");
        theatreDTO.setAddress("Brigade Road, Bangalore");
        theatreDTO.setTotalSeats(150);
        theatreDTO.setCityId(20L);
        theatreDTO.setCityName("Bangalore");

        assertEquals(2L, theatreDTO.getId());
        assertEquals("INOX", theatreDTO.getName());
        assertEquals("Brigade Road, Bangalore", theatreDTO.getAddress());
        assertEquals(150, theatreDTO.getTotalSeats());
        assertEquals(20L, theatreDTO.getCityId());
        assertEquals("Bangalore", theatreDTO.getCityName());
    }

}
