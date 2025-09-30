package com.company.mtbp.inventory.service;

import com.company.mtbp.inventory.dto.MovieDTO;
import com.company.mtbp.inventory.dto.ShowDTO;
import com.company.mtbp.inventory.entity.Movie;
import com.company.mtbp.inventory.entity.Show;
import com.company.mtbp.inventory.entity.Theatre;
import com.company.mtbp.inventory.exception.ResourceNotFoundException;
import com.company.mtbp.inventory.mapper.ShowMapper;
import com.company.mtbp.inventory.repository.MovieRepository;
import com.company.mtbp.inventory.repository.ShowRepository;
import com.company.mtbp.inventory.repository.TheatreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShowServiceTest {

    @Mock
    private ShowRepository showRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private TheatreRepository theatreRepository;

    @Mock
    private ShowMapper showMapper;

    @Mock
    private SeatService seatService;

    @InjectMocks
    private ShowService showService;

    private Show sampleShow;
    private ShowDTO sampleShowDTO;
    private Movie sampleMovie;
    private MovieDTO sampleMovieDTO;
    private Theatre sampleTheatre;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleMovie = new Movie();
        sampleMovie.setId(1L);

        sampleTheatre = new Theatre();
        sampleTheatre.setId(1L);

        sampleShow = new Show();
        sampleShow.setId(1L);
        sampleShow.setMovie(sampleMovie);
        sampleShow.setTheatre(sampleTheatre);
        sampleShow.setShowDate(LocalDate.now());
        sampleShow.setStartTime(LocalTime.of(18, 0));

        sampleMovieDTO = new MovieDTO();
        sampleMovieDTO.setId(1L);

        sampleShowDTO = new ShowDTO();
        sampleShowDTO.setId(1L);
        sampleShowDTO.setMovieId(1L);
        sampleShowDTO.setTheatreId(1L);
        sampleShowDTO.setShowDate(LocalDate.now());
        sampleShowDTO.setStartTime(LocalTime.of(18, 0));
    }

    @Test
    void saveShow_success() {
        when(showMapper.toEntity(sampleShowDTO)).thenReturn(sampleShow);
        when(movieRepository.findById(1L)).thenReturn(Optional.of(sampleMovie));
        when(theatreRepository.findById(1L)).thenReturn(Optional.of(sampleTheatre));
        when(showRepository.save(sampleShow)).thenReturn(sampleShow);
        when(seatService.updateShowForTheatre(1L, 1L)).thenReturn(10);
        when(showMapper.toDTO(sampleShow)).thenReturn(sampleShowDTO);

        ShowDTO result = showService.saveShow(sampleShowDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(showRepository).save(sampleShow);
        verify(seatService).updateShowForTheatre(1L, 1L);
    }

    @Test
    void saveShow_movieNotFound_throws() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> showService.saveShow(sampleShowDTO));
    }

    @Test
    void saveShow_theatreNotFound_throws() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(sampleMovie));
        when(theatreRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NullPointerException.class, () -> showService.saveShow(sampleShowDTO));
    }

    @Test
    void patchShow_success() {
        Map<String, Object> updates = Map.of("showDate", LocalDate.now().plusDays(1).toString());

        when(showMapper.toEntity(any())).thenReturn(sampleShow);
        when(movieRepository.findById(1L)).thenReturn(Optional.of(sampleMovie));
        when(theatreRepository.findById(1L)).thenReturn(Optional.of(sampleTheatre));
        when(showRepository.save(sampleShow)).thenReturn(sampleShow);
        when(seatService.updateShowForTheatre(1L, 1L)).thenReturn(5);
        when(showMapper.toDTO(sampleShow)).thenReturn(sampleShowDTO);

        ShowDTO updated = showService.patchShow(sampleShowDTO, updates);

        assertNotNull(updated);
        verify(showRepository).save(sampleShow);
        verify(seatService).updateShowForTheatre(1L, 1L);
    }

    @Test
    void getShows_success() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(sampleMovie));
        when(showRepository.findAll(any(Specification.class))).thenReturn(List.of(sampleShow));
        when(showMapper.toDTOList(List.of(sampleShow))).thenReturn(List.of(sampleShowDTO));

        List<ShowDTO> shows = showService.getShows(sampleMovieDTO, "Mumbai", LocalDate.now());
        assertEquals(1, shows.size());
        assertEquals(1L, shows.getFirst().getId());
    }

    @Test
    void getShowById_found() {
        when(showRepository.findById(1L)).thenReturn(Optional.of(sampleShow));
        when(showMapper.toDTO(sampleShow)).thenReturn(sampleShowDTO);

        Optional<ShowDTO> dto = showService.getShowById(1L);

        assertTrue(dto.isPresent());
        assertEquals(1L, dto.get().getId());
    }

    @Test
    void getShowById_notFound() {
        when(showRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<ShowDTO> dto = showService.getShowById(1L);
        assertTrue(dto.isEmpty());
    }

    @Test
    void deleteShow_callsRepository() {
        assertDoesNotThrow(() -> showService.deleteShow(1L));
        verify(showRepository).deleteById(1L);
    }
}
