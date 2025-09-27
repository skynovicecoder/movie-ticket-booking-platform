package com.company.mtbp.inventory.component;

import com.company.mtbp.inventory.entity.City;
import com.company.mtbp.inventory.entity.Movie;
import com.company.mtbp.inventory.entity.Theatre;
import com.company.mtbp.inventory.repository.CityRepository;
import com.company.mtbp.inventory.repository.MovieRepository;
import com.company.mtbp.inventory.repository.TheatreRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class Initialization {
    private final TheatreRepository theatreRepository;
    private final MovieRepository movieRepository;
    private final CityRepository cityRepository;

    @PostConstruct
    public void init() {

        City cityObj = new City();
        cityObj.setName("Hyderabad");
        cityRepository.save(cityObj);

        // Create Theatres one by one
        Theatre theatreObj = new Theatre();
        theatreObj.setName("In-orbit Hyderabad");
        theatreObj.setAddress("Hi-Tech City, Hyderabad");
        theatreObj.setTotalSeats(200);
        theatreObj.setCity(cityObj);
        theatreRepository.save(theatreObj);

        // Create cities
        City city1 = new City();
        city1.setName("Mumbai");

        City city2 = new City();
        city2.setName("Pune");
        cityRepository.saveAll(Arrays.asList(city1, city2));

        // Create Theatres
        Theatre theatre1 = new Theatre();
        theatre1.setName("Cineplex Mumbai Central");
        theatre1.setAddress("123 Central Street, Mumbai");
        theatre1.setTotalSeats(200);
        theatre1.setCity(city1);

        Theatre theatre2 = new Theatre();
        theatre2.setName("IMAX Mumbai");
        theatre2.setAddress("456 Marine Drive, Mumbai");
        theatre2.setTotalSeats(150);
        theatre2.setCity(city1);

        Theatre theatre3 = new Theatre();
        theatre3.setName("Pune Galaxy");
        theatre3.setAddress("789 FC Road, Pune");
        theatre3.setTotalSeats(180);
        theatre3.setCity(city2);

        theatreRepository.saveAll(Arrays.asList(theatre1, theatre2, theatre3));


        // Create Movies

        Movie movieObj = new Movie(null, "Pirates Of Caribbean", "Action-Comedy", 120, "English",
                LocalDate.of(2019, 4, 26), "PG-13", null);
        movieRepository.save(movieObj);

        Movie movie1 = new Movie(null, "Avengers: Endgame", "Action", 181, "English",
                LocalDate.of(2019, 4, 26), "PG-13", null);
        Movie movie2 = new Movie(null, "Inception", "Sci-Fi", 148, "English",
                LocalDate.of(2010, 7, 16), "PG-13", null);
        Movie movie3 = new Movie(null, "Interstellar", "Sci-Fi", 169, "English",
                LocalDate.of(2014, 11, 7), "PG-13", null);
        Movie movie4 = new Movie(null, "3 Idiots", "Drama", 170, "Hindi",
                LocalDate.of(2009, 12, 25), "PG", null);
        Movie movie5 = new Movie(null, "Avatar", "Sci-Fi", 162, "English",
                LocalDate.of(2009, 12, 18), "PG-13", null);

        movieRepository.saveAll(Arrays.asList(movie1, movie2, movie3, movie4, movie5));
    }

}
