package com.company.mtbp.inventory.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MovieDTOTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        LocalDate releaseDate = LocalDate.of(2025, 10, 1);
        MovieDTO movie = new MovieDTO(1L, "Inception", "Sci-Fi", 148, "English", releaseDate, "PG-13");

        assertEquals(1L, movie.getId());
        assertEquals("Inception", movie.getTitle());
        assertEquals("Sci-Fi", movie.getGenre());
        assertEquals(148, movie.getDurationMinutes());
        assertEquals("English", movie.getLanguage());
        assertEquals(releaseDate, movie.getReleaseDate());
        assertEquals("PG-13", movie.getRating());
    }

    @Test
    void testSetters() {
        MovieDTO movie = new MovieDTO();
        LocalDate releaseDate = LocalDate.of(2025, 11, 5);

        movie.setId(2L);
        movie.setTitle("Interstellar");
        movie.setGenre("Sci-Fi");
        movie.setDurationMinutes(169);
        movie.setLanguage("English");
        movie.setReleaseDate(releaseDate);
        movie.setRating("PG-13");

        assertEquals(2L, movie.getId());
        assertEquals("Interstellar", movie.getTitle());
        assertEquals("Sci-Fi", movie.getGenre());
        assertEquals(169, movie.getDurationMinutes());
        assertEquals("English", movie.getLanguage());
        assertEquals(releaseDate, movie.getReleaseDate());
        assertEquals("PG-13", movie.getRating());
    }

    @Test
    void testBuilder() {
        LocalDate releaseDate = LocalDate.of(2025, 12, 25);
        MovieDTO movie = MovieDTO.builder()
                .id(3L)
                .title("Avatar 2")
                .genre("Action")
                .durationMinutes(192)
                .language("English")
                .releaseDate(releaseDate)
                .rating("PG-13")
                .build();

        assertEquals(3L, movie.getId());
        assertEquals("Avatar 2", movie.getTitle());
        assertEquals("Action", movie.getGenre());
        assertEquals(192, movie.getDurationMinutes());
        assertEquals("English", movie.getLanguage());
        assertEquals(releaseDate, movie.getReleaseDate());
        assertEquals("PG-13", movie.getRating());
    }

}
