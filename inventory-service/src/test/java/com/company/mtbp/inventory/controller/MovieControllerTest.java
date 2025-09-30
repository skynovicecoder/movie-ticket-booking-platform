package com.company.mtbp.inventory.controller;

import com.company.mtbp.inventory.dto.MovieDTO;
import com.company.mtbp.inventory.service.MovieService;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class MovieControllerTest {

    @Mock
    private MovieService movieService;

    @InjectMocks
    private MovieController movieController;

    private MockMvc mockMvc;
    private Faker faker;
    private MovieDTO sampleMovie;

    @BeforeEach
    void setup() {
        faker = new Faker();
        mockMvc = MockMvcBuilders.standaloneSetup(movieController).build();

        sampleMovie = new MovieDTO();
        sampleMovie.setId(1L);
        sampleMovie.setTitle(faker.book().title());
        sampleMovie.setGenre("Drama");
        sampleMovie.setDurationMinutes(120);
    }

    @Test
    void createMovie_returnsCreatedMovie() throws Exception {
        Mockito.when(movieService.saveMovie(any(MovieDTO.class))).thenReturn(sampleMovie);

        mockMvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"" + sampleMovie.getTitle() + "\"," +
                                "\"genre\":\"" + sampleMovie.getGenre() + "\"," +
                                "\"duration\":120}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(sampleMovie.getId()))
                .andExpect(jsonPath("$.title").value(sampleMovie.getTitle()))
                .andExpect(jsonPath("$.genre").value(sampleMovie.getGenre()));
    }

    @Test
    void patchMovie_updatesMovie() throws Exception {
        MovieDTO updated = new MovieDTO();
        updated.setId(sampleMovie.getId());
        updated.setTitle("Updated Title");
        updated.setGenre(sampleMovie.getGenre());
        updated.setDurationMinutes(sampleMovie.getDurationMinutes());

        Mockito.when(movieService.getMovieById(1L)).thenReturn(Optional.of(sampleMovie));
        Mockito.when(movieService.patchMovie(any(MovieDTO.class), any(Map.class))).thenReturn(updated);

        mockMvc.perform(patch("/api/movies/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Title\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    void patchMovie_returnsNotFound_whenMovieMissing() throws Exception {
        Mockito.when(movieService.getMovieById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(patch("/api/movies/update/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Title\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllMovies_returnsList() throws Exception {
        List<MovieDTO> movies = List.of(sampleMovie);
        Mockito.when(movieService.getAllMovies()).thenReturn(movies);

        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(movies.size()))
                .andExpect(jsonPath("$[0].title").value(sampleMovie.getTitle()));
    }

    @Test
    void getMovieById_returnsMovie() throws Exception {
        Mockito.when(movieService.getMovieById(1L)).thenReturn(Optional.of(sampleMovie));

        mockMvc.perform(get("/api/movies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleMovie.getId()))
                .andExpect(jsonPath("$.title").value(sampleMovie.getTitle()));
    }

    @Test
    void getMovieById_returnsNotFound_whenMovieMissing() throws Exception {
        Mockito.when(movieService.getMovieById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/movies/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getMovieByTitle_returnsMovie() throws Exception {
        Mockito.when(movieService.getMovieByTitle(sampleMovie.getTitle()))
                .thenReturn(Optional.of(sampleMovie));

        mockMvc.perform(get("/api/movies/by-title")
                        .param("title", sampleMovie.getTitle()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(sampleMovie.getTitle()));
    }

    @Test
    void getMovieByTitle_returnsNotFound_whenMovieMissing() throws Exception {
        Mockito.when(movieService.getMovieByTitle("Unknown")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/movies/by-title")
                        .param("title", "Unknown"))
                .andExpect(status().isNotFound());
    }
}
