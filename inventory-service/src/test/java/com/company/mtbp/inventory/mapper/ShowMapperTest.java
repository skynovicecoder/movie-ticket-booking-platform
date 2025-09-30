package com.company.mtbp.inventory.mapper;

import com.company.mtbp.inventory.dto.ShowDTO;
import com.company.mtbp.inventory.entity.Movie;
import com.company.mtbp.inventory.entity.Show;
import com.company.mtbp.inventory.entity.Theatre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ShowMapperTest {

    private ShowMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(ShowMapper.class);
    }

    @Test
    void toDTO_shouldMapShowToDTO() {
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Inception");

        Theatre theatre = new Theatre();
        theatre.setId(10L);
        theatre.setName("IMAX");

        Show show = new Show();
        show.setId(100L);
        show.setMovie(movie);
        show.setTheatre(theatre);
        show.setShowDate(LocalDate.of(2025, 10, 30));
        show.setStartTime(LocalTime.of(18, 30));

        ShowDTO dto = mapper.toDTO(show);

        assertNotNull(dto);
        assertEquals(100L, dto.getId());
        assertEquals(1L, dto.getMovieId());
        assertEquals("Inception", dto.getMovieTitle());
        assertEquals(10L, dto.getTheatreId());
        assertEquals("IMAX", dto.getTheatreName());
        assertEquals(LocalDate.of(2025, 10, 30), dto.getShowDate());
        assertEquals(LocalTime.of(18, 30), dto.getStartTime());
    }

    @Test
    void toEntity_shouldMapDTOToShow() {
        ShowDTO dto = new ShowDTO();
        dto.setId(200L);
        dto.setMovieId(2L);
        dto.setTheatreId(20L);
        dto.setShowDate(LocalDate.of(2025, 11, 1));
        dto.setStartTime(LocalTime.of(20, 0));

        Show entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(200L, entity.getId());
        assertEquals(2L, entity.getMovie().getId());
        assertEquals(20L, entity.getTheatre().getId());
        assertEquals(LocalDate.of(2025, 11, 1), entity.getShowDate());
        assertEquals(LocalTime.of(20, 0), entity.getStartTime());
    }

    @Test
    void toDTOList_shouldMapListOfShows() {
        Show show1 = new Show();
        show1.setId(1L);
        Show show2 = new Show();
        show2.setId(2L);

        List<ShowDTO> dtoList = mapper.toDTOList(List.of(show1, show2));

        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());
        assertEquals(1L, dtoList.get(0).getId());
        assertEquals(2L, dtoList.get(1).getId());
    }
}
