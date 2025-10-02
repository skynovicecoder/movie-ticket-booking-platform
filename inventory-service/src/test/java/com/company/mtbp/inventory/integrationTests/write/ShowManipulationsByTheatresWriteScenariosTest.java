package com.company.mtbp.inventory.integrationTests.write;

import com.company.mtbp.inventory.dto.ShowDTO;
import com.company.mtbp.inventory.entity.Movie;
import com.company.mtbp.inventory.entity.Show;
import com.company.mtbp.inventory.entity.Theatre;
import com.company.mtbp.inventory.mapper.ShowMapper;
import com.company.mtbp.inventory.repository.MovieRepository;
import com.company.mtbp.inventory.repository.ShowRepository;
import com.company.mtbp.inventory.repository.TheatreRepository;
import com.company.mtbp.inventory.service.SeatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@EnabledIfSystemProperty(named = "env", matches = "dev")
public class ShowManipulationsByTheatresWriteScenariosTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShowMapper showMapper;

    @MockitoBean
    private ShowRepository showRepository;

    @MockitoBean
    private MovieRepository movieRepository;

    @MockitoBean
    private TheatreRepository theatreRepository;

    @MockitoBean
    private SeatService seatService;

    private ObjectMapper objectMapper;

    private Movie movie;
    private Theatre theatre;
    private Show show;
    private ShowDTO showDTO;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        movie = Movie.builder().id(1L).title("Avengers").build();
        theatre = Theatre.builder().id(1L).name("PVR").build();

        show = Show.builder()
                .id(1L)
                .movie(movie)
                .theatre(theatre)
                .showDate(LocalDate.of(2025, 10, 1))
                .startTime(LocalTime.NOON)
                .endTime(LocalTime.NOON.plusHours(3))
                .showType("2D")
                .build();

        showDTO = showMapper.toDTO(show);
        showDTO.setMovieId(movie.getId());
        showDTO.setTheatreId(theatre.getId());
    }

    @Test
    void createShow_shouldReturnCreatedShow() throws Exception {
        Mockito.when(movieRepository.findById(movie.getId())).thenReturn(Optional.of(movie));
        Mockito.when(theatreRepository.findById(theatre.getId())).thenReturn(Optional.of(theatre));
        Mockito.when(showRepository.save(any(Show.class))).thenReturn(show);
        Mockito.when(seatService.updateShowForTheatre(anyLong(), anyLong())).thenReturn(5);

        mockMvc.perform(post("/api/v1/shows")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(showDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(show.getId()))
                .andExpect(jsonPath("$.movieId").value(movie.getId()))
                .andExpect(jsonPath("$.theatreId").value(theatre.getId()));
    }

    @Test
    void patchShow_shouldReturnUpdatedShow() throws Exception {
        Mockito.when(showRepository.findById(show.getId())).thenReturn(Optional.of(show));
        Mockito.when(movieRepository.findById(movie.getId())).thenReturn(Optional.of(movie));
        Mockito.when(theatreRepository.findById(theatre.getId())).thenReturn(Optional.of(theatre));
        Show newShow = Show.builder()
                .id(1L)
                .movie(movie)
                .theatre(theatre)
                .showDate(LocalDate.of(2025, 10, 2))
                .startTime(LocalTime.NOON.plusHours(8))
                .endTime(LocalTime.NOON.plusHours(11))
                .showType("2D")
                .build();
        Mockito.when(showRepository.save(any(Show.class))).thenReturn(newShow);
        Mockito.when(seatService.updateShowForTheatre(anyLong(), anyLong())).thenReturn(5);

        String patchJson = "{ \"showDate\": \"2025-10-02\", \"startTime\": \"20:00\", \"endTime\": \"23:00\" }";

        mockMvc.perform(patch("/api/v1/shows/update/{showId}", show.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.showDate").value("2025-10-02"))
                .andExpect(jsonPath("$.startTime").value("20:00:00"))
                .andExpect(jsonPath("$.endTime").value("23:00:00"));
    }

    @Test
    void deleteShow_shouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(showRepository).deleteById(show.getId());

        mockMvc.perform(delete("/api/v1/shows/{showId}", show.getId()))
                .andExpect(status().isNoContent());
    }
}
