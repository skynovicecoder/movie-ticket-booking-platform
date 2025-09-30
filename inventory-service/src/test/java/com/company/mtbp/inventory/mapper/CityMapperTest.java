package com.company.mtbp.inventory.mapper;

import com.company.mtbp.inventory.dto.CityDTO;
import com.company.mtbp.inventory.entity.City;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CityMapperTest {

    private CityMapper cityMapper;

    @BeforeEach
    void setup() {
        cityMapper = Mappers.getMapper(CityMapper.class);
    }

    @Test
    void toDTO_shouldMapCityToCityDTO() {
        City city = new City();
        city.setId(1L);
        city.setName("Mumbai");

        CityDTO dto = cityMapper.toDTO(city);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Mumbai", dto.getName());
    }

    @Test
    void toEntity_shouldMapCityDTOToCity() {
        CityDTO dto = new CityDTO();
        dto.setId(2L);
        dto.setName("Pune");

        City city = cityMapper.toEntity(dto);

        assertNotNull(city);
        assertEquals(2L, city.getId());
        assertEquals("Pune", city.getName());
    }

    @Test
    void toDTOList_shouldMapCityListToCityDTOList() {
        City city1 = new City();
        city1.setId(1L);
        city1.setName("Mumbai");

        City city2 = new City();
        city2.setId(2L);
        city2.setName("Pune");

        List<CityDTO> dtoList = cityMapper.toDTOList(List.of(city1, city2));

        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());
        assertEquals("Mumbai", dtoList.get(0).getName());
        assertEquals("Pune", dtoList.get(1).getName());
    }

    @Test
    void toDTOList_shouldReturnEmptyListForEmptyInput() {
        List<CityDTO> dtoList = cityMapper.toDTOList(List.of());
        assertNotNull(dtoList);
        assertTrue(dtoList.isEmpty());
    }
}
