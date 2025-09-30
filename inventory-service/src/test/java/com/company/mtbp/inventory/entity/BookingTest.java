package com.company.mtbp.inventory.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BookingTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        Booking booking = new Booking();

        Customer customer = new Customer();
        Show show = new Show();
        BookingDetail detail = new BookingDetail();

        booking.setId(1L);
        booking.setCustomer(customer);
        booking.setShow(show);
        booking.setBookingTime(LocalDateTime.of(2025, 1, 1, 10, 0));
        booking.setTotalAmount(250.0);
        booking.setStatus("BOOKED");
        booking.setBookingDetails(List.of(detail));

        assertThat(booking.getId()).isEqualTo(1L);
        assertThat(booking.getCustomer()).isSameAs(customer);
        assertThat(booking.getShow()).isSameAs(show);
        assertThat(booking.getBookingTime()).isEqualTo("2025-01-01T10:00");
        assertThat(booking.getTotalAmount()).isEqualTo(250.0);
        assertThat(booking.getStatus()).isEqualTo("BOOKED");
        assertThat(booking.getBookingDetails()).containsExactly(detail);
    }

    @Test
    void testAllArgsConstructor() {
        Customer customer = new Customer();
        Show show = new Show();
        BookingDetail detail = new BookingDetail();

        Booking booking = new Booking(
                2L,
                customer,
                show,
                LocalDateTime.of(2025, 2, 1, 12, 30),
                500.0,
                "CANCELLED",
                List.of(detail)
        );

        assertThat(booking.getId()).isEqualTo(2L);
        assertThat(booking.getCustomer()).isSameAs(customer);
        assertThat(booking.getShow()).isSameAs(show);
        assertThat(booking.getBookingTime()).isEqualTo("2025-02-01T12:30");
        assertThat(booking.getTotalAmount()).isEqualTo(500.0);
        assertThat(booking.getStatus()).isEqualTo("CANCELLED");
        assertThat(booking.getBookingDetails()).containsExactly(detail);
    }

    @Test
    void testBuilder() {
        Customer customer = new Customer();
        Show show = new Show();
        BookingDetail detail = new BookingDetail();

        Booking booking = Booking.builder()
                .id(3L)
                .customer(customer)
                .show(show)
                .bookingTime(LocalDateTime.of(2025, 3, 10, 18, 0))
                .totalAmount(750.0)
                .status("BOOKED")
                .bookingDetails(List.of(detail))
                .build();

        assertThat(booking.getId()).isEqualTo(3L);
        assertThat(booking.getCustomer()).isSameAs(customer);
        assertThat(booking.getShow()).isSameAs(show);
        assertThat(booking.getBookingTime()).isEqualTo("2025-03-10T18:00");
        assertThat(booking.getTotalAmount()).isEqualTo(750.0);
        assertThat(booking.getStatus()).isEqualTo("BOOKED");
        assertThat(booking.getBookingDetails()).containsExactly(detail);
    }

}
