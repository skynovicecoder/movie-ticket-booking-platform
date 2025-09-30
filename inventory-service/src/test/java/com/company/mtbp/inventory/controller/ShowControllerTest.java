package com.company.mtbp.inventory.controller;

import com.company.mtbp.inventory.dto.MovieDTO;
import com.company.mtbp.inventory.dto.ShowDTO;
import com.company.mtbp.inventory.dto.TheatreDTO;
import com.company.mtbp.inventory.exception.ResourceNotFoundException;
import com.company.mtbp.inventory.mapper.MovieMapper;
import com.company.mtbp.inventory.mapper.ShowMapper;
import com.company.mtbp.inventory.repository.MovieRepository;
import com.company.mtbp.inventory.repository.ShowRepository;
import com.company.mtbp.inventory.service.MovieService;
import com.company.mtbp.inventory.service.ShowService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
@ActiveProfiles("test")
class ShowControllerTest {

    @Mock
    private MovieService movieService;

    @Mock
    private ShowService showService;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ShowRepository showRepository;

    @Mock
    private ShowMapper showMapper;

    @Mock
    private MovieMapper movieMapper;

    @InjectMocks
    private ShowController showController;

    private MockMvc mockMvc;
    private Faker faker;
    private ObjectMapper objectMapper;
    private ShowDTO sampleShow;
    private MovieDTO sampleMovie;

    @BeforeEach
    void setup() {
        faker = new Faker();
        mockMvc = MockMvcBuilders.standaloneSetup(showController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        sampleMovie = new MovieDTO();
        sampleMovie.setId(1L);
        sampleMovie.setTitle("Interstellar");

        sampleShow = new ShowDTO();
        sampleShow.setId(100L);
        sampleShow.setMovieTitle(sampleMovie.getTitle());
        sampleShow.setShowDate(LocalDate.now());
        sampleShow.setStartTime(LocalTime.of(18, 0));
    }

    @Test
    void browseShows_returnsShowsForMovieAndCity() throws Exception {
        TheatreDTO theatreDto = new TheatreDTO();
        theatreDto.setId(200L);
        theatreDto.setName(faker.company().name() + " Theatre");
        theatreDto.setCityName("Mumbai");
        theatreDto.setAddress(faker.address().fullAddress());

        MovieDTO movieDto = new MovieDTO();
        movieDto.setId(2L);
        movieDto.setTitle("Interstellar");

        List<ShowDTO> showsDto = IntStream.range(0, 3)
                .mapToObj(i -> {
                    ShowDTO show = new ShowDTO();
                    show.setId(faker.number().randomNumber());
                    show.setMovieTitle(movieDto.getTitle());
                    show.setShowDate(LocalDate.now());
                    show.setStartTime(LocalTime.of(18, 0));
                    return show;
                })
                .toList();

        Mockito.when(movieService.getMovieByTitle(movieDto.getTitle()))
                .thenReturn(Optional.of(movieDto));
        Mockito.when(showService.getShows(eq(movieDto), eq(theatreDto.getCityName()), any()))
                .thenReturn(showsDto);

        //MvcResult result = mockMvc.perform(get("/api/shows/browse")
        mockMvc.perform(get("/api/shows/browse")
                        .param("movieTitle", movieDto.getTitle())
                        .param("cityName", theatreDto.getCityName()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(showsDto.size()))
                .andExpect(jsonPath("$[0].movieTitle").value(movieDto.getTitle()))
                .andReturn();

        //String responseBody = result.getResponse().getContentAsString();
        //log.info("Response JSON: {}", responseBody);
    }

    @Test
    void browseShows_returnsShows() throws Exception {
        TheatreDTO theatreDto = new TheatreDTO();
        theatreDto.setId(100L);
        theatreDto.setName(faker.company().name() + " Theatre");
        theatreDto.setCityName(faker.address().city());
        theatreDto.setAddress(faker.address().fullAddress());

        MovieDTO movieDto = new MovieDTO();
        movieDto.setId(1L);
        movieDto.setTitle(faker.book().title());

        List<ShowDTO> showsDto = IntStream.range(0, 3)
                .mapToObj(i -> {
                    ShowDTO show = new ShowDTO();
                    show.setId(faker.number().randomNumber());
                    show.setMovieTitle(movieDto.getTitle());
                    show.setShowDate(LocalDate.now());
                    show.setStartTime(LocalTime.MIDNIGHT);
                    return show;
                })
                .toList();

        Mockito.when(movieService.getMovieByTitle(movieDto.getTitle()))
                .thenReturn(Optional.of(movieDto));
        Mockito.when(showService.getShows(eq(movieDto), any(), any()))
                .thenReturn(showsDto);

        mockMvc.perform(get("/api/shows/browse")
                        .param("movieTitle", movieDto.getTitle()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(showsDto.size()));
    }

    @Test
    void browseShows_returnsNoContent_whenNoShowsFound() throws Exception {
        MovieDTO movieDto = new MovieDTO();
        movieDto.setId(2L);
        movieDto.setTitle(faker.book().title());

        Mockito.when(movieService.getMovieByTitle(movieDto.getTitle()))
                .thenReturn(Optional.of(movieDto));
        Mockito.when(showService.getShows(eq(movieDto), any(), any()))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/shows/browse")
                        .param("movieTitle", movieDto.getTitle()))
                .andExpect(status().isNoContent());
    }

    @Test
    void browseShows_throwsNotFound_whenMovieDoesNotExist() throws Exception {
        String unknownTitle = faker.book().title();

        Mockito.when(movieService.getMovieByTitle(unknownTitle))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            showController.browseShows(unknownTitle, null, null);
        });
    }

    @Test
    void createShow_returnsCreatedShow() throws Exception {
        Mockito.when(showService.saveShow(any(ShowDTO.class))).thenReturn(sampleShow);

        mockMvc.perform(post("/api/shows")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleShow)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(sampleShow.getId()))
                .andExpect(jsonPath("$.movieTitle").value(sampleShow.getMovieTitle()));
    }

    @Test
    void browseShows_throwsWhenMovieNotFound() {
        Mockito.when(movieService.getMovieByTitle("Unknown"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                showController.browseShows("Unknown", null, null)
        );
    }

    @Test
    void patchShow_updatesShow() throws Exception {
        Mockito.when(showService.getShowById(100L)).thenReturn(Optional.of(sampleShow));

        ShowDTO updated = new ShowDTO();
        updated.setId(100L);
        updated.setMovieTitle("Interstellar");
        updated.setShowDate(LocalDate.now().plusDays(1));
        updated.setStartTime(LocalTime.of(20, 0));

        Mockito.when(showService.patchShow(eq(sampleShow), any(Map.class))).thenReturn(updated);

        mockMvc.perform(patch("/api/shows/update/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"showDate\":\"" + LocalDate.now().plusDays(1) + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100));
    }

    @Test
    void patchShow_throwsWhenNotFound() {
        Mockito.when(showService.getShowById(999L)).thenReturn(Optional.empty());

        Map<String, Object> updates = Map.of("showDate", "2025-01-01");

        assertThrows(ResourceNotFoundException.class, () ->
                showController.patchShow(999L, updates)
        );
    }

    @Test
    void getShowById_returnsShow() throws Exception {
        Mockito.when(showService.getShowById(100L)).thenReturn(Optional.of(sampleShow));

        mockMvc.perform(get("/api/shows/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.movieTitle").value("Interstellar"));
    }

    @Test
    void getShowById_returnsNotFound() throws Exception {
        Mockito.when(showService.getShowById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/shows/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteShow_returnsNoContent() throws Exception {
        Mockito.doNothing().when(showService).deleteShow(100L);

        mockMvc.perform(delete("/api/shows/100"))
                .andExpect(status().isNoContent());
    }

    @Test
    void create_Show_returns_CreatedShow() throws Exception {
        Mockito.when(showService.saveShow(any(ShowDTO.class))).thenReturn(sampleShow);

        mockMvc.perform(post("/api/shows")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleShow)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(sampleShow.getId()))
                .andExpect(jsonPath("$.movieTitle").value(sampleShow.getMovieTitle()));
    }

    @Test
    void browse_Shows_returns_Shows() throws Exception {
        Mockito.when(movieService.getMovieByTitle("Interstellar")).thenReturn(Optional.of(sampleMovie));
        Mockito.when(showService.getShows(eq(sampleMovie), eq("Mumbai"), any()))
                .thenReturn(List.of(sampleShow));

        mockMvc.perform(get("/api/shows/browse")
                        .param("movieTitle", "Interstellar")
                        .param("cityName", "Mumbai"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].movieTitle").value("Interstellar"));
    }

    @Test
    void browse_Shows_returns_NoContent_when_NoShowsFound() throws Exception {
        Mockito.when(movieService.getMovieByTitle("Interstellar")).thenReturn(Optional.of(sampleMovie));
        Mockito.when(showService.getShows(eq(sampleMovie), any(), any())).thenReturn(List.of());

        mockMvc.perform(get("/api/shows/browse")
                        .param("movieTitle", "Interstellar"))
                .andExpect(status().isNoContent());
    }
}
