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
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @BeforeEach
    void setup() {
        faker = new Faker();
        mockMvc = MockMvcBuilders.standaloneSetup(showController).build();
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
}
