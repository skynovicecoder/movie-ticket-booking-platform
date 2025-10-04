package com.company.mtbp.partner.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SeatRequestTest {

    @Test
    void testNoArgsConstructorAndSettersGetters() {
        SeatRequest seat = new SeatRequest();

        seat.setSeatNumber("A1");
        seat.setSeatType("VIP");
        seat.setAvailable(true);
        seat.setTheatreId(100L);
        seat.setShowId(200L);

        assertThat(seat.getSeatNumber()).isEqualTo("A1");
        assertThat(seat.getSeatType()).isEqualTo("VIP");
        assertThat(seat.isAvailable()).isTrue();
        assertThat(seat.getTheatreId()).isEqualTo(100L);
        assertThat(seat.getShowId()).isEqualTo(200L);
    }

    @Test
    void testAllArgsConstructor() {
        SeatRequest seat = new SeatRequest("B2", "Regular", false, 101L, 201L);

        assertThat(seat.getSeatNumber()).isEqualTo("B2");
        assertThat(seat.getSeatType()).isEqualTo("Regular");
        assertThat(seat.isAvailable()).isFalse();
        assertThat(seat.getTheatreId()).isEqualTo(101L);
        assertThat(seat.getShowId()).isEqualTo(201L);
    }
}
