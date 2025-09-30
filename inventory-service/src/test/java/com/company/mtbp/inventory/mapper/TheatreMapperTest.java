package com.company.mtbp.inventory.mapper;

import com.company.mtbp.inventory.dto.TheatreDTO;
import com.company.mtbp.inventory.entity.City;
import com.company.mtbp.inventory.entity.Theatre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TheatreMapperTest {

    private TheatreMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(TheatreMapper.class);
    }

    @Test
    void toDTO_shouldMapTheatreToDTO() {
        City city = new City();
        city.setId(1L);
        city.setName("Pune");

        Theatre theatre = new Theatre();
        theatre.setId(100L);
        theatre.setName("IMAX");
        theatre.setCity(city);

        TheatreDTO dto = mapper.toDTO(theatre);

        assertNotNull(dto);
        assertEquals(100L, dto.getId());
        assertEquals("IMAX", dto.getName());
        assertEquals(1L, dto.getCityId());
        assertEquals("Pune", dto.getCityName());
    }

    @Test
    void toEntity_shouldMapDTOToTheatre() {
        TheatreDTO dto = new TheatreDTO();
        dto.setId(200L);
        dto.setName("PVR");
        dto.setCityId(2L);
        dto.setCityName("Mumbai");

        Theatre entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(200L, entity.getId());
        assertEquals("PVR", entity.getName());
    }

    @Test
    void toDTOList_shouldMapListOfTheatres() {
        Theatre theatre1 = new Theatre();
        theatre1.setId(1L);
        Theatre theatre2 = new Theatre();
        theatre2.setId(2L);

        List<TheatreDTO> dtoList = mapper.toDTOList(List.of(theatre1, theatre2));

        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());
        assertEquals(1L, dtoList.get(0).getId());
        assertEquals(2L, dtoList.get(1).getId());
    }
}
