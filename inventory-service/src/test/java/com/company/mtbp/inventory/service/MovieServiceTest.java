package com.company.mtbp.inventory.service;

import com.company.mtbp.inventory.dto.MovieDTO;
import com.company.mtbp.inventory.entity.Movie;
import com.company.mtbp.inventory.exception.BadRequestException;
import com.company.mtbp.inventory.mapper.MovieMapper;
import com.company.mtbp.inventory.repository.MovieRepository;
import com.company.mtbp.shared.dto.PageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private MovieMapper movieMapper;

    @InjectMocks
    private MovieService movieService;

    private Movie sampleMovie;
    private MovieDTO sampleMovieDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleMovie = Movie.builder()
                .id(1L)
                .title("Inception")
                .releaseDate(LocalDate.of(2010, 7, 16))
                .durationMinutes(148)
                .build();

        sampleMovieDTO = MovieDTO.builder()
                .id(1L)
                .title("Inception")
                .releaseDate(LocalDate.of(2010, 7, 16))
                .durationMinutes(148)
                .build();
    }

    @Test
    void saveMovie_success() {
        when(movieMapper.toEntity(sampleMovieDTO)).thenReturn(sampleMovie);
        when(movieRepository.save(sampleMovie)).thenReturn(sampleMovie);
        when(movieMapper.toDTO(sampleMovie)).thenReturn(sampleMovieDTO);

        MovieDTO result = movieService.saveMovie(sampleMovieDTO);

        assertNotNull(result);
        assertEquals("Inception", result.getTitle());
        verify(movieRepository).save(sampleMovie);
    }

    @Test
    void patchMovie_updatesFieldsSuccessfully() {
        when(movieMapper.toEntity(sampleMovieDTO)).thenReturn(sampleMovie);
        when(movieRepository.save(sampleMovie)).thenReturn(sampleMovie);
        when(movieMapper.toDTO(sampleMovie)).thenReturn(sampleMovieDTO);

        MovieDTO patched = movieService.patchMovie(sampleMovieDTO,
                Map.of("title", "Interstellar", "durationMinutes", 169, "releaseDate", "2014-11-07"));

        assertNotNull(patched);
        verify(movieRepository).save(sampleMovie);
    }

    @Test
    void patchMovie_invalidField_throwsBadRequest() {
        assertThrows(BadRequestException.class,
                () -> movieService.patchMovie(sampleMovieDTO, Map.of("invalidField", "value")));
    }

    @Test
    void getAllMovies_success() {
        Page<Movie> moviePage = new PageImpl<>(List.of(sampleMovie));
        when(movieRepository.findAll(PageRequest.of(0, 10))).thenReturn(moviePage);
        when(movieMapper.toDTOList(moviePage.getContent())).thenReturn(List.of(sampleMovieDTO));

        PageResponse<MovieDTO> result = movieService.getAllMovies(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(sampleMovieDTO.getTitle(), result.getContent().getFirst().getTitle());
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(0, result.getPageNumber());
        assertTrue(result.isLast());
    }

    @Test
    void getMovieById_found() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(sampleMovie));
        when(movieMapper.toDTO(sampleMovie)).thenReturn(sampleMovieDTO);

        Optional<MovieDTO> result = movieService.getMovieById(1L);

        assertTrue(result.isPresent());
        assertEquals("Inception", result.get().getTitle());
    }

    @Test
    void getMovieById_notFound() {
        when(movieRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<MovieDTO> result = movieService.getMovieById(2L);

        assertTrue(result.isEmpty());
    }

    @Test
    void getMovieByTitle_found() {
        when(movieRepository.findByTitle("Inception")).thenReturn(Optional.of(sampleMovie));
        when(movieMapper.toDTO(sampleMovie)).thenReturn(sampleMovieDTO);

        Optional<MovieDTO> result = movieService.getMovieByTitle("Inception");

        assertTrue(result.isPresent());
        assertEquals("Inception", result.get().getTitle());
    }

    @Test
    void getMovieByTitle_notFound() {
        when(movieRepository.findByTitle("Unknown")).thenReturn(Optional.empty());

        Optional<MovieDTO> result = movieService.getMovieByTitle("Unknown");

        assertTrue(result.isEmpty());
    }
}
