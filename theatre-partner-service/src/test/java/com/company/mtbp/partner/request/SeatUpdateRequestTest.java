package com.company.mtbp.partner.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SeatUpdateRequestTest {

    @Test
    void testNoArgsConstructorAndSettersGetters() {
        SeatUpdateRequest seatUpdate = new SeatUpdateRequest();

        seatUpdate.setSeatNumber("A1");
        seatUpdate.setSeatType("VIP");
        seatUpdate.setAvailable(true);

        assertThat(seatUpdate.getSeatNumber()).isEqualTo("A1");
        assertThat(seatUpdate.getSeatType()).isEqualTo("VIP");
        assertThat(seatUpdate.getAvailable()).isTrue();
    }

    @Test
    void testAllArgsConstructor() {
        SeatUpdateRequest seatUpdate = new SeatUpdateRequest("B2", "Regular", false);

        assertThat(seatUpdate.getSeatNumber()).isEqualTo("B2");
        assertThat(seatUpdate.getSeatType()).isEqualTo("Regular");
        assertThat(seatUpdate.getAvailable()).isFalse();
    }
}
