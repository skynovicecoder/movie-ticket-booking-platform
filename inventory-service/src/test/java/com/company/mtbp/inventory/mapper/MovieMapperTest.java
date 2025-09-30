package com.company.mtbp.inventory.mapper;

import com.company.mtbp.inventory.dto.MovieDTO;
import com.company.mtbp.inventory.entity.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MovieMapperTest {

    private MovieMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(MovieMapper.class);
    }

    @Test
    void toDTO_shouldMapEntityToDTO() {
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Inception");
        movie.setDurationMinutes(150);

        MovieDTO dto = mapper.toDTO(movie);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Inception", dto.getTitle());
        assertEquals(150, dto.getDurationMinutes());
    }

    @Test
    void toEntity_shouldMapDTOToEntity() {
        MovieDTO dto = new MovieDTO();
        dto.setId(2L);
        dto.setTitle("Interstellar");
        dto.setDurationMinutes(170);

        Movie entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(2L, entity.getId());
        assertEquals("Interstellar", entity.getTitle());
        assertEquals(170, entity.getDurationMinutes());
    }

    @Test
    void toDTOList_shouldMapListOfEntities() {
        Movie movie1 = new Movie();
        movie1.setId(1L);
        movie1.setTitle("Movie1");

        Movie movie2 = new Movie();
        movie2.setId(2L);
        movie2.setTitle("Movie2");

        List<MovieDTO> dtoList = mapper.toDTOList(List.of(movie1, movie2));

        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());
        assertEquals("Movie1", dtoList.get(0).getTitle());
        assertEquals("Movie2", dtoList.get(1).getTitle());
    }
}
