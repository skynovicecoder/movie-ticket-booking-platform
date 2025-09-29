package com.company.mtbp.inventory.component;

import com.company.mtbp.inventory.entity.*;
import com.company.mtbp.inventory.repository.*;
import com.company.mtbp.inventory.service.TheatreService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Profile("dev")
public class Initialization {

    private final TheatreRepository theatreRepository;
    private final MovieRepository movieRepository;
    private final CityRepository cityRepository;
    private final SeatRepository seatRepository;
    private final TheatreService theatreService;
    private final CustomerRepository customerRepository;

    @PostConstruct
    public void init() {
        List<City> cities = createCities();
        List<Theatre> theatres = createTheatres(cities);
        createSeatsForTheatres(theatres);
        createMovies();
        createCustomers();
    }

    private List<City> createCities() {
        City hyderabad = new City(null, "Hyderabad", null);
        City mumbai = new City(null, "Mumbai", null);
        City pune = new City(null, "Pune", null);

        List<City> cities = List.of(hyderabad, mumbai, pune);
        return cityRepository.saveAll(cities);
    }

    private List<Theatre> createTheatres(List<City> cities) {
        City hyderabad = cities.stream().filter(c -> c.getName().equals("Hyderabad")).findFirst().orElseThrow();
        City mumbai = cities.stream().filter(c -> c.getName().equals("Mumbai")).findFirst().orElseThrow();
        City pune = cities.stream().filter(c -> c.getName().equals("Pune")).findFirst().orElseThrow();

        List<Theatre> theatres = new ArrayList<>();

        theatres.add(new Theatre(null, "In-orbit Hyderabad", "Hi-Tech City, Hyderabad", 10, hyderabad, null, null));
        theatres.add(new Theatre(null, "Cineplex Mumbai Central", "123 Central Street, Mumbai", 10, mumbai, null, null));
        theatres.add(new Theatre(null, "IMAX Mumbai", "456 Marine Drive, Mumbai", 10, mumbai, null, null));
        theatres.add(new Theatre(null, "Pune Galaxy", "789 FC Road, Pune", 10, pune, null, null));

        return theatreRepository.saveAll(theatres);
    }

    private void createSeatsForTheatres(List<Theatre> theatres) {
        for (Theatre theatre : theatres) {
            int totalSeats = theatre.getTotalSeats();
            List<Seat> allSeats = theatreService.prepareSeatsForTheatre(theatre, totalSeats);
            seatRepository.saveAll(allSeats);
        }
    }

    private void createMovies() {
        List<Movie> movies = List.of(
                new Movie(null, "Pirates Of Caribbean", "Action-Comedy", 120, "English",
                        LocalDate.of(2019, 4, 26), "PG-13", null),
                new Movie(null, "Avengers: Endgame", "Action", 181, "English",
                        LocalDate.of(2019, 4, 26), "PG-13", null),
                new Movie(null, "Inception", "Sci-Fi", 148, "English",
                        LocalDate.of(2010, 7, 16), "PG-13", null),
                new Movie(null, "Interstellar", "Sci-Fi", 169, "English",
                        LocalDate.of(2014, 11, 7), "PG-13", null),
                new Movie(null, "3 Idiots", "Drama", 170, "Hindi",
                        LocalDate.of(2009, 12, 25), "PG", null),
                new Movie(null, "Avatar", "Sci-Fi", 162, "English",
                        LocalDate.of(2009, 12, 18), "PG-13", null)
        );

        movieRepository.saveAll(movies);
    }

    private void createCustomers() {
        List<Customer> customers = List.of(
                new Customer(null, "Gokhu San", "gokhu.san@dragonballz.com", "9876543210", null),
                new Customer(null, "Naruto Uzimaki", "naruto.uzimaki@ninzawar.com", "9123456780", null),
                new Customer(null, "Luffy D Monkey", "luffy.d.monkey@onepiece.com", "9988776655", null)
        );

        customerRepository.saveAll(customers);
    }
}
