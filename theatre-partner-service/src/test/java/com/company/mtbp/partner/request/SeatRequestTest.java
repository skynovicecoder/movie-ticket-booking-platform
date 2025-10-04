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

    @Test
    void testEqualsAndHashCode() {
        SeatRequest seat1 = new SeatRequest("C3", "VIP", true, 102L, 202L);
        SeatRequest seat2 = new SeatRequest("C3", "VIP", true, 102L, 202L);
        SeatRequest seat3 = new SeatRequest("D4", "Regular", false, 103L, 203L);

        assertThat(seat1).isEqualTo(seat2);
        assertThat(seat1.hashCode()).isEqualTo(seat2.hashCode());

        assertThat(seat1).isNotEqualTo(seat3);
        assertThat(seat1.hashCode()).isNotEqualTo(seat3.hashCode());
    }

    @Test
    void testToString() {
        SeatRequest seat = new SeatRequest("E5", "Regular", true, 104L, 204L);

        String str = seat.toString();
        assertThat(str).contains("E5", "Regular", "true", "104", "204");
    }
}
