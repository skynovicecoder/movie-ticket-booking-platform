package com.company.mtbp.inventory.integrationTests;

import com.company.mtbp.inventory.entity.City;
import com.company.mtbp.inventory.entity.Movie;
import com.company.mtbp.inventory.entity.Show;
import com.company.mtbp.inventory.entity.Theatre;
import com.company.mtbp.inventory.mapper.ShowMapper;
import com.company.mtbp.inventory.repository.MovieRepository;
import com.company.mtbp.inventory.repository.ShowRepository;
import com.company.mtbp.inventory.service.ShowService;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@ActiveProfiles("test")
class ShowControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShowService showService;

    @Autowired
    private ShowMapper showMapper;

    @MockitoBean
    private MovieRepository movieRepository;

    @MockitoBean
    private ShowRepository showRepository;

    private Faker faker;

    @BeforeEach
    void setup() {
        faker = new Faker();
    }

    @Test
    void browseShows_returnsShows() throws Exception {
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Inception");

        List<Show> shows = IntStream.range(0, 2)
                .mapToObj(i -> {
                    Show s = new Show();
                    s.setId((long) i + 1);
                    s.setMovie(movie);
                    s.setShowDate(LocalDate.now());
                    return s;
                }).toList();

        Mockito.when(movieRepository.findByTitle(movie.getTitle()))
                .thenReturn(Optional.of(movie));
        Mockito.when(movieRepository.findById(movie.getId()))
                .thenReturn(Optional.of(movie));
        Mockito.when(showRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(shows);

        String responseBody = mockMvc.perform(get("/api/shows/browse")
                        .param("movieTitle", movie.getTitle()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(shows.size()))
                .andExpect(jsonPath("$[0].movieTitle").value(movie.getTitle()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        log.info("Response JSON: {}", responseBody);
    }

    @Test
    void browseShows_returnsShows_byMovieAndCity() throws Exception {
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Inception");

        String cityName = "Mumbai";
        List<Show> shows = IntStream.range(0, 2)
                .mapToObj(i -> {
                    Show s = new Show();
                    s.setId((long) i + 1);
                    s.setMovie(movie);
                    s.setShowDate(LocalDate.now());

                    Theatre theatre = new Theatre();
                    theatre.setId((long) i + 1);
                    theatre.setName("PVR");

                    City city = new City();
                    city.setName("Mumbai");
                    theatre.setCity(city);
                    s.setTheatre(theatre);

                    return s;
                }).toList();

        Mockito.when(movieRepository.findByTitle(movie.getTitle()))
                .thenReturn(Optional.of(movie));
        Mockito.when(movieRepository.findById(movie.getId()))
                .thenReturn(Optional.of(movie));
        Mockito.when(showRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(shows);

        mockMvc.perform(get("/api/shows/browse")
                        .param("movieTitle", movie.getTitle())
                        .param("cityName", cityName))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(shows.size()))
                .andExpect(jsonPath("$[0].movieTitle").value(movie.getTitle()))
                .andExpect(jsonPath("$[0].theatreName").value("PVR"));
    }
}

