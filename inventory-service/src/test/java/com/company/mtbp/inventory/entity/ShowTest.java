package com.company.mtbp.inventory.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ShowTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        Show show = new Show();
        Movie movie = new Movie();
        Theatre theatre = new Theatre();
        Seat seat = new Seat();
        BookingDetail detail = new BookingDetail();

        show.setId(1L);
        show.setMovie(movie);
        show.setTheatre(theatre);
        show.setShowDate(LocalDate.of(2025, 10, 1));
        show.setStartTime(LocalTime.of(10, 0));
        show.setEndTime(LocalTime.of(12, 0));
        show.setPricePerTicket(200.0);
        show.setShowType("morning");
        show.setSeats(List.of(seat));
        show.setBookingDetails(List.of(detail));

        assertThat(show.getId()).isEqualTo(1L);
        assertThat(show.getMovie()).isSameAs(movie);
        assertThat(show.getTheatre()).isSameAs(theatre);
        assertThat(show.getShowDate()).isEqualTo(LocalDate.of(2025, 10, 1));
        assertThat(show.getStartTime()).isEqualTo(LocalTime.of(10, 0));
        assertThat(show.getEndTime()).isEqualTo(LocalTime.of(12, 0));
        assertThat(show.getPricePerTicket()).isEqualTo(200.0);
        assertThat(show.getShowType()).isEqualTo("morning");
        assertThat(show.getSeats()).containsExactly(seat);
        assertThat(show.getBookingDetails()).containsExactly(detail);
    }

    @Test
    void testAllArgsConstructor() {
        Movie movie = new Movie();
        Theatre theatre = new Theatre();
        Seat seat = new Seat();
        BookingDetail detail = new BookingDetail();

        Show show = new Show(
                2L,
                movie,
                theatre,
                LocalDate.of(2025, 11, 15),
                LocalTime.of(14, 0),
                LocalTime.of(16, 0),
                300.0,
                "afternoon",
                List.of(seat),
                List.of(detail)
        );

        assertThat(show.getId()).isEqualTo(2L);
        assertThat(show.getMovie()).isSameAs(movie);
        assertThat(show.getTheatre()).isSameAs(theatre);
        assertThat(show.getShowDate()).isEqualTo(LocalDate.of(2025, 11, 15));
        assertThat(show.getStartTime()).isEqualTo(LocalTime.of(14, 0));
        assertThat(show.getEndTime()).isEqualTo(LocalTime.of(16, 0));
        assertThat(show.getPricePerTicket()).isEqualTo(300.0);
        assertThat(show.getShowType()).isEqualTo("afternoon");
        assertThat(show.getSeats()).containsExactly(seat);
        assertThat(show.getBookingDetails()).containsExactly(detail);
    }

    @Test
    void testBuilder() {
        Movie movie = new Movie();
        Theatre theatre = new Theatre();
        Seat seat = new Seat();
        BookingDetail detail = new BookingDetail();

        Show show = Show.builder()
                .id(3L)
                .movie(movie)
                .theatre(theatre)
                .showDate(LocalDate.of(2025, 12, 25))
                .startTime(LocalTime.of(18, 0))
                .endTime(LocalTime.of(20, 30))
                .pricePerTicket(400.0)
                .showType("evening")
                .seats(List.of(seat))
                .bookingDetails(List.of(detail))
                .build();

        assertThat(show.getId()).isEqualTo(3L);
        assertThat(show.getMovie()).isSameAs(movie);
        assertThat(show.getTheatre()).isSameAs(theatre);
        assertThat(show.getShowDate()).isEqualTo(LocalDate.of(2025, 12, 25));
        assertThat(show.getStartTime()).isEqualTo(LocalTime.of(18, 0));
        assertThat(show.getEndTime()).isEqualTo(LocalTime.of(20, 30));
        assertThat(show.getPricePerTicket()).isEqualTo(400.0);
        assertThat(show.getShowType()).isEqualTo("evening");
        assertThat(show.getSeats()).containsExactly(seat);
        assertThat(show.getBookingDetails()).containsExactly(detail);
    }

}
