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

    @Test
    void testBuilder() {
        SeatUpdateRequest seatUpdate = SeatUpdateRequest.builder()
                .seatNumber("C3")
                .seatType("Premium")
                .available(true)
                .build();

        assertThat(seatUpdate.getSeatNumber()).isEqualTo("C3");
        assertThat(seatUpdate.getSeatType()).isEqualTo("Premium");
        assertThat(seatUpdate.getAvailable()).isTrue();
    }

    @Test
    void testEqualsAndHashCode() {
        SeatUpdateRequest seat1 = new SeatUpdateRequest("D4", "VIP", true);
        SeatUpdateRequest seat2 = new SeatUpdateRequest("D4", "VIP", true);
        SeatUpdateRequest seat3 = new SeatUpdateRequest("E5", "Regular", false);

        assertThat(seat1).isEqualTo(seat2);
        assertThat(seat1.hashCode()).isEqualTo(seat2.hashCode());

        assertThat(seat1).isNotEqualTo(seat3);
        assertThat(seat1.hashCode()).isNotEqualTo(seat3.hashCode());
    }

    @Test
    void testToString() {
        SeatUpdateRequest seatUpdate = new SeatUpdateRequest("F6", "Regular", true);

        String str = seatUpdate.toString();
        assertThat(str).contains("F6", "Regular", "true");
    }
}
