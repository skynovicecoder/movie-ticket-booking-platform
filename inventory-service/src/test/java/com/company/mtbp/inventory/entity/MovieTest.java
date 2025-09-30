package com.company.mtbp.inventory.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MovieTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        Movie movie = new Movie();
        Show show = new Show();

        movie.setId(1L);
        movie.setTitle("Inception");
        movie.setGenre("Sci-Fi");
        movie.setDurationMinutes(148);
        movie.setLanguage("English");
        movie.setReleaseDate(LocalDate.of(2010, 7, 16));
        movie.setRating("PG-13");
        movie.setShows(List.of(show));

        assertThat(movie.getId()).isEqualTo(1L);
        assertThat(movie.getTitle()).isEqualTo("Inception");
        assertThat(movie.getGenre()).isEqualTo("Sci-Fi");
        assertThat(movie.getDurationMinutes()).isEqualTo(148);
        assertThat(movie.getLanguage()).isEqualTo("English");
        assertThat(movie.getReleaseDate()).isEqualTo(LocalDate.of(2010, 7, 16));
        assertThat(movie.getRating()).isEqualTo("PG-13");
        assertThat(movie.getShows()).containsExactly(show);
    }

    @Test
    void testAllArgsConstructor() {
        Show show = new Show();

        Movie movie = new Movie(
                2L,
                "The Dark Knight",
                "Action",
                152,
                "English",
                LocalDate.of(2008, 7, 18),
                "PG-13",
                List.of(show)
        );

        assertThat(movie.getId()).isEqualTo(2L);
        assertThat(movie.getTitle()).isEqualTo("The Dark Knight");
        assertThat(movie.getGenre()).isEqualTo("Action");
        assertThat(movie.getDurationMinutes()).isEqualTo(152);
        assertThat(movie.getLanguage()).isEqualTo("English");
        assertThat(movie.getReleaseDate()).isEqualTo(LocalDate.of(2008, 7, 18));
        assertThat(movie.getRating()).isEqualTo("PG-13");
        assertThat(movie.getShows()).containsExactly(show);
    }

    @Test
    void testBuilder() {
        Show show = new Show();

        Movie movie = Movie.builder()
                .id(3L)
                .title("Interstellar")
                .genre("Sci-Fi")
                .durationMinutes(169)
                .language("English")
                .releaseDate(LocalDate.of(2014, 11, 7))
                .rating("PG-13")
                .shows(List.of(show))
                .build();

        assertThat(movie.getId()).isEqualTo(3L);
        assertThat(movie.getTitle()).isEqualTo("Interstellar");
        assertThat(movie.getGenre()).isEqualTo("Sci-Fi");
        assertThat(movie.getDurationMinutes()).isEqualTo(169);
        assertThat(movie.getLanguage()).isEqualTo("English");
        assertThat(movie.getReleaseDate()).isEqualTo(LocalDate.of(2014, 11, 7));
        assertThat(movie.getRating()).isEqualTo("PG-13");
        assertThat(movie.getShows()).containsExactly(show);
    }

}
