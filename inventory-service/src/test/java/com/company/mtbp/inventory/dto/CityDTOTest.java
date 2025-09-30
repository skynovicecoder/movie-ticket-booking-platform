package com.company.mtbp.inventory.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CityDTOTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        CityDTO city = new CityDTO(1L, "Mumbai");

        assertEquals(1L, city.getId());
        assertEquals("Mumbai", city.getName());
    }

    @Test
    void testSetters() {
        CityDTO city = new CityDTO();
        city.setId(2L);
        city.setName("Delhi");

        assertEquals(2L, city.getId());
        assertEquals("Delhi", city.getName());
    }

    @Test
    void testBuilder() {
        CityDTO city = CityDTO.builder()
                .id(3L)
                .name("Bangalore")
                .build();

        assertEquals(3L, city.getId());
        assertEquals("Bangalore", city.getName());
    }

}
