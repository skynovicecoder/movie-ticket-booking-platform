package com.company.mtbp.partner.request;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class ShowRequestTest {

    @Test
    void testNoArgsConstructorAndSettersGetters() {
        ShowRequest show = new ShowRequest();

        show.setMovieId(101L);
        show.setTheatreId(10L);
        show.setShowDate(LocalDate.of(2025, 10, 5));
        show.setStartTime(LocalTime.of(18, 0));
        show.setEndTime(LocalTime.of(20, 30));
        show.setPricePerTicket(250.0);
        show.setShowType("IMAX");

        assertThat(show.getMovieId()).isEqualTo(101L);
        assertThat(show.getTheatreId()).isEqualTo(10L);
        assertThat(show.getShowDate()).isEqualTo(LocalDate.of(2025, 10, 5));
        assertThat(show.getStartTime()).isEqualTo(LocalTime.of(18, 0));
        assertThat(show.getEndTime()).isEqualTo(LocalTime.of(20, 30));
        assertThat(show.getPricePerTicket()).isEqualTo(250.0);
        assertThat(show.getShowType()).isEqualTo("IMAX");
    }

    @Test
    void testAllArgsConstructor() {
        ShowRequest show = new ShowRequest(
                102L,
                20L,
                LocalDate.of(2025, 11, 1),
                LocalTime.of(19, 0),
                LocalTime.of(21, 0),
                300.0,
                "3D"
        );

        assertThat(show.getMovieId()).isEqualTo(102L);
        assertThat(show.getTheatreId()).isEqualTo(20L);
        assertThat(show.getShowDate()).isEqualTo(LocalDate.of(2025, 11, 1));
        assertThat(show.getStartTime()).isEqualTo(LocalTime.of(19, 0));
        assertThat(show.getEndTime()).isEqualTo(LocalTime.of(21, 0));
        assertThat(show.getPricePerTicket()).isEqualTo(300.0);
        assertThat(show.getShowType()).isEqualTo("3D");
    }

    @Test
    void testEqualsAndHashCode() {
        ShowRequest show1 = new ShowRequest(101L, 10L,
                LocalDate.of(2025, 10, 5),
                LocalTime.of(18, 0),
                LocalTime.of(20, 30),
                250.0,
                "IMAX");

        ShowRequest show2 = new ShowRequest(101L, 10L,
                LocalDate.of(2025, 10, 5),
                LocalTime.of(18, 0),
                LocalTime.of(20, 30),
                250.0,
                "IMAX");

        ShowRequest show3 = new ShowRequest(102L, 11L,
                LocalDate.of(2025, 10, 6),
                LocalTime.of(19, 0),
                LocalTime.of(21, 0),
                300.0,
                "3D");

        assertThat(show1).isEqualTo(show2);
        assertThat(show1.hashCode()).isEqualTo(show2.hashCode());

        assertThat(show1).isNotEqualTo(show3);
        assertThat(show1.hashCode()).isNotEqualTo(show3.hashCode());
    }

    @Test
    void testToString() {
        ShowRequest show = new ShowRequest(
                101L,
                10L,
                LocalDate.of(2025, 10, 5),
                LocalTime.of(18, 0),
                LocalTime.of(20, 30),
                250.0,
                "IMAX"
        );

        String str = show.toString();
        assertThat(str).contains("101", "10", "2025-10-05", "18:00", "20:30", "250.0", "IMAX");
    }
}
