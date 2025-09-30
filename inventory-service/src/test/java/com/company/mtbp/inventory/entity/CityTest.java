package com.company.mtbp.inventory.entity;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CityTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        City city = new City();
        Theatre theatre = new Theatre();

        city.setId(1L);
        city.setName("Pune");
        city.setTheatres(List.of(theatre));

        assertThat(city.getId()).isEqualTo(1L);
        assertThat(city.getName()).isEqualTo("Pune");
        assertThat(city.getTheatres()).containsExactly(theatre);
    }

    @Test
    void testAllArgsConstructor() {
        Theatre theatre = new Theatre();

        City city = new City(
                2L,
                "Mumbai",
                List.of(theatre)
        );

        assertThat(city.getId()).isEqualTo(2L);
        assertThat(city.getName()).isEqualTo("Mumbai");
        assertThat(city.getTheatres()).containsExactly(theatre);
    }

    @Test
    void testBuilder() {
        Theatre theatre = new Theatre();

        City city = City.builder()
                .id(3L)
                .name("Bangalore")
                .theatres(List.of(theatre))
                .build();

        assertThat(city.getId()).isEqualTo(3L);
        assertThat(city.getName()).isEqualTo("Bangalore");
        assertThat(city.getTheatres()).containsExactly(theatre);
    }

}
