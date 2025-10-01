package com.company.mtbp.inventory.events;

import com.company.mtbp.inventory.entity.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class InventoryCreatedEventTest {

    @Test
    void testRecordGetters() {
        Customer customer = Customer.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .build();

        Movie movie = Movie.builder()
                .id(101L)
                .title("Movie Night")
                .build();

        Theatre theatre = Theatre.builder()
                .id(201L)
                .name("Cineplex")
                .build();

        Show show = Show.builder()
                .id(301L)
                .movie(movie)
                .theatre(theatre)
                .showDate(LocalDate.of(2025, 10, 1))
                .startTime(LocalTime.of(20, 0))
                .endTime(LocalTime.of(22, 0))
                .pricePerTicket(150.0)
                .showType("Evening")
                .build();

        Seat seat1 = Seat.builder().id(1L).seatNumber("A1").build();
        Seat seat2 = Seat.builder().id(2L).seatNumber("A2").build();

        BookingDetail detail1 = BookingDetail.builder()
                .id(1L)
                .show(show)
                .seat(seat1)
                .price(150.0)
                .discountApplied(0.0)
                .build();

        BookingDetail detail2 = BookingDetail.builder()
                .id(2L)
                .show(show)
                .seat(seat2)
                .price(150.0)
                .discountApplied(0.0)
                .build();

        LocalDateTime bookingTime = LocalDateTime.of(2025, 10, 1, 19, 30);

        InventoryCreatedEvent event = new InventoryCreatedEvent(
                500L,
                customer,
                show,
                bookingTime,
                300.0,
                "CONFIRMED",
                List.of(detail1, detail2)
        );

        assertThat(event.id()).isEqualTo(500L);
        assertThat(event.customer()).isEqualTo(customer);
        assertThat(event.show()).isEqualTo(show);
        assertThat(event.bookingTime()).isEqualTo(bookingTime);
        assertThat(event.totalAmount()).isEqualTo(300.0);
        assertThat(event.status()).isEqualTo("CONFIRMED");
        assertThat(event.bookingDetails()).containsExactly(detail1, detail2);

        String eventStr = event.toString();
        assertThat(eventStr).contains("500", "CONFIRMED");
    }

    @Test
    void testEqualityAndHashCode() {
        Customer customer = Customer.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .build();

        Movie movie = Movie.builder().id(101L).title("Movie Night").build();
        Theatre theatre = Theatre.builder().id(201L).name("Cineplex").build();
        Show show = Show.builder().id(301L).movie(movie).theatre(theatre).build();

        BookingDetail detail = BookingDetail.builder()
                .id(1L)
                .show(show)
                .price(150.0)
                .discountApplied(0.0)
                .build();

        LocalDateTime now = LocalDateTime.now();
        InventoryCreatedEvent event1 = new InventoryCreatedEvent(1L, customer, show, now, 150.0, "CONFIRMED", List.of(detail));
        InventoryCreatedEvent event2 = new InventoryCreatedEvent(1L, customer, show, now, 150.0, "CONFIRMED", List.of(detail));

        assertThat(event1).isEqualTo(event2);
        assertThat(event1.hashCode()).isEqualTo(event2.hashCode());
    }
}
