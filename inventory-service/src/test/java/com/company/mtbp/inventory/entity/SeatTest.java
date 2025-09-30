package com.company.mtbp.inventory.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SeatTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        Seat seat = new Seat();
        Theatre theatre = new Theatre();
        Show show = new Show();

        seat.setId(1L);
        seat.setSeatNumber("R1");
        seat.setSeatType("REGULAR");
        seat.setAvailable(false);
        seat.setTheatre(theatre);
        seat.setShow(show);

        assertThat(seat.getId()).isEqualTo(1L);
        assertThat(seat.getSeatNumber()).isEqualTo("R1");
        assertThat(seat.getSeatType()).isEqualTo("REGULAR");
        assertThat(seat.getAvailable()).isFalse();
        assertThat(seat.getTheatre()).isSameAs(theatre);
        assertThat(seat.getShow()).isSameAs(show);
    }

    @Test
    void testAllArgsConstructor() {
        Theatre theatre = new Theatre();
        Show show = new Show();

        Seat seat = new Seat(
                2L,
                "P1",
                "PREMIUM",
                true,
                theatre,
                show
        );

        assertThat(seat.getId()).isEqualTo(2L);
        assertThat(seat.getSeatNumber()).isEqualTo("P1");
        assertThat(seat.getSeatType()).isEqualTo("PREMIUM");
        assertThat(seat.getAvailable()).isTrue();
        assertThat(seat.getTheatre()).isSameAs(theatre);
        assertThat(seat.getShow()).isSameAs(show);
    }

    @Test
    void testBuilder() {
        Theatre theatre = new Theatre();
        Show show = new Show();

        Seat seat = Seat.builder()
                .id(3L)
                .seatNumber("V1")
                .seatType("VIP")
                .available(true)
                .theatre(theatre)
                .show(show)
                .build();

        assertThat(seat.getId()).isEqualTo(3L);
        assertThat(seat.getSeatNumber()).isEqualTo("V1");
        assertThat(seat.getSeatType()).isEqualTo("VIP");
        assertThat(seat.getAvailable()).isTrue();
        assertThat(seat.getTheatre()).isSameAs(theatre);
        assertThat(seat.getShow()).isSameAs(show);
    }

    @Test
    void testDefaultAvailableIsTrue() {
        Seat seat = new Seat();
        assertThat(seat.getAvailable()).isTrue();
    }

}
