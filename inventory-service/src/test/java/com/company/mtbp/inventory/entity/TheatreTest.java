package com.company.mtbp.inventory.entity;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TheatreTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        Theatre theatre = new Theatre();
        City city = new City();
        Show show = new Show();
        Seat seat = new Seat();

        theatre.setId(1L);
        theatre.setName("PVR Pune");
        theatre.setAddress("MG Road, Pune");
        theatre.setTotalSeats(120);
        theatre.setCity(city);
        theatre.setShows(List.of(show));
        theatre.setSeats(List.of(seat));

        assertThat(theatre.getId()).isEqualTo(1L);
        assertThat(theatre.getName()).isEqualTo("PVR Pune");
        assertThat(theatre.getAddress()).isEqualTo("MG Road, Pune");
        assertThat(theatre.getTotalSeats()).isEqualTo(120);
        assertThat(theatre.getCity()).isSameAs(city);
        assertThat(theatre.getShows()).containsExactly(show);
        assertThat(theatre.getSeats()).containsExactly(seat);
    }

    @Test
    void testAllArgsConstructor() {
        City city = new City();
        Show show = new Show();
        Seat seat = new Seat();

        Theatre theatre = new Theatre(
                2L,
                "INOX Mumbai",
                "Bandra West, Mumbai",
                150,
                city,
                List.of(show),
                List.of(seat)
        );

        assertThat(theatre.getId()).isEqualTo(2L);
        assertThat(theatre.getName()).isEqualTo("INOX Mumbai");
        assertThat(theatre.getAddress()).isEqualTo("Bandra West, Mumbai");
        assertThat(theatre.getTotalSeats()).isEqualTo(150);
        assertThat(theatre.getCity()).isSameAs(city);
        assertThat(theatre.getShows()).containsExactly(show);
        assertThat(theatre.getSeats()).containsExactly(seat);
    }

    @Test
    void testBuilder() {
        City city = new City();
        Show show = new Show();
        Seat seat = new Seat();

        Theatre theatre = Theatre.builder()
                .id(3L)
                .name("Carnival Bangalore")
                .address("Koramangala, Bangalore")
                .totalSeats(200)
                .city(city)
                .shows(List.of(show))
                .seats(List.of(seat))
                .build();

        assertThat(theatre.getId()).isEqualTo(3L);
        assertThat(theatre.getName()).isEqualTo("Carnival Bangalore");
        assertThat(theatre.getAddress()).isEqualTo("Koramangala, Bangalore");
        assertThat(theatre.getTotalSeats()).isEqualTo(200);
        assertThat(theatre.getCity()).isSameAs(city);
        assertThat(theatre.getShows()).containsExactly(show);
        assertThat(theatre.getSeats()).containsExactly(seat);
    }

}
