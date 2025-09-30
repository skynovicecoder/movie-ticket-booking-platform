package com.company.mtbp.inventory.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class ShowDTOTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        ShowDTO showDTO = new ShowDTO(
                1L,
                10L,
                "Avengers",
                100L,
                "PVR Cinema",
                LocalDate.of(2025, 10, 30),
                LocalTime.of(18, 0),
                LocalTime.of(21, 0),
                250.0,
                "3D"
        );

        assertEquals(1L, showDTO.getId());
        assertEquals(10L, showDTO.getMovieId());
        assertEquals("Avengers", showDTO.getMovieTitle());
        assertEquals(100L, showDTO.getTheatreId());
        assertEquals("PVR Cinema", showDTO.getTheatreName());
        assertEquals(LocalDate.of(2025, 10, 30), showDTO.getShowDate());
        assertEquals(LocalTime.of(18, 0), showDTO.getStartTime());
        assertEquals(LocalTime.of(21, 0), showDTO.getEndTime());
        assertEquals(250.0, showDTO.getPricePerTicket());
        assertEquals("3D", showDTO.getShowType());
    }

    @Test
    void testSetters() {
        ShowDTO showDTO = new ShowDTO();
        showDTO.setId(2L);
        showDTO.setMovieId(20L);
        showDTO.setMovieTitle("Spider-Man");
        showDTO.setTheatreId(200L);
        showDTO.setTheatreName("INOX");
        showDTO.setShowDate(LocalDate.of(2025, 11, 1));
        showDTO.setStartTime(LocalTime.of(15, 0));
        showDTO.setEndTime(LocalTime.of(18, 0));
        showDTO.setPricePerTicket(300.0);
        showDTO.setShowType("IMAX");

        assertEquals(2L, showDTO.getId());
        assertEquals(20L, showDTO.getMovieId());
        assertEquals("Spider-Man", showDTO.getMovieTitle());
        assertEquals(200L, showDTO.getTheatreId());
        assertEquals("INOX", showDTO.getTheatreName());
        assertEquals(LocalDate.of(2025, 11, 1), showDTO.getShowDate());
        assertEquals(LocalTime.of(15, 0), showDTO.getStartTime());
        assertEquals(LocalTime.of(18, 0), showDTO.getEndTime());
        assertEquals(300.0, showDTO.getPricePerTicket());
        assertEquals("IMAX", showDTO.getShowType());
    }

}
