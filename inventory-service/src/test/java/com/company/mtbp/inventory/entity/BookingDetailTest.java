package com.company.mtbp.inventory.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BookingDetailTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        BookingDetail detail = new BookingDetail();

        Booking booking = new Booking();
        Seat seat = new Seat();
        Show show = new Show();

        detail.setId(1L);
        detail.setBooking(booking);
        detail.setSeat(seat);
        detail.setShow(show);
        detail.setPrice(200.0);
        detail.setDiscountApplied(20.0);

        assertThat(detail.getId()).isEqualTo(1L);
        assertThat(detail.getBooking()).isSameAs(booking);
        assertThat(detail.getSeat()).isSameAs(seat);
        assertThat(detail.getShow()).isSameAs(show);
        assertThat(detail.getPrice()).isEqualTo(200.0);
        assertThat(detail.getDiscountApplied()).isEqualTo(20.0);
    }

    @Test
    void testAllArgsConstructor() {
        Booking booking = new Booking();
        Seat seat = new Seat();
        Show show = new Show();

        BookingDetail detail = new BookingDetail(
                2L,
                booking,
                seat,
                show,
                300.0,
                50.0
        );

        assertThat(detail.getId()).isEqualTo(2L);
        assertThat(detail.getBooking()).isSameAs(booking);
        assertThat(detail.getSeat()).isSameAs(seat);
        assertThat(detail.getShow()).isSameAs(show);
        assertThat(detail.getPrice()).isEqualTo(300.0);
        assertThat(detail.getDiscountApplied()).isEqualTo(50.0);
    }

    @Test
    void testBuilder() {
        Booking booking = new Booking();
        Seat seat = new Seat();
        Show show = new Show();

        BookingDetail detail = BookingDetail.builder()
                .id(3L)
                .booking(booking)
                .seat(seat)
                .show(show)
                .price(400.0)
                .discountApplied(75.0)
                .build();

        assertThat(detail.getId()).isEqualTo(3L);
        assertThat(detail.getBooking()).isSameAs(booking);
        assertThat(detail.getSeat()).isSameAs(seat);
        assertThat(detail.getShow()).isSameAs(show);
        assertThat(detail.getPrice()).isEqualTo(400.0);
        assertThat(detail.getDiscountApplied()).isEqualTo(75.0);
    }

}
