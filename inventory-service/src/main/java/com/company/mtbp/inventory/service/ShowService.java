package com.company.mtbp.inventory.service;

import com.company.mtbp.inventory.entity.City;
import com.company.mtbp.inventory.entity.Movie;
import com.company.mtbp.inventory.entity.Show;
import com.company.mtbp.inventory.entity.Theatre;
import com.company.mtbp.inventory.repository.ShowRepository;
import com.company.mtbp.inventory.specifications.ShowSpecifications;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class ShowService {

    private final ShowRepository showRepository;

    public ShowService(ShowRepository showRepository) {
        this.showRepository = showRepository;
    }

    // Browse shows for a movie in selected theatres on a given date
    /*public List<Show> getShows(Movie movie, List<Theatre> theatres, LocalDate date) {
        return showRepository.findByMovieAndTheatreInAndShowDate(movie, theatres, date);
    }*/
    public List<Show> getShows(Movie movie, String cityName, LocalDate date) {
        Specification<Show> spec = ShowSpecifications.byMovie(movie)
                .and(ShowSpecifications.byCity(cityName))
                .and(ShowSpecifications.byDate(date));

        return showRepository.findAll(spec);
    }

    // Get a show by its ID
    public Optional<Show> getShowById(Long id) {
        return showRepository.findById(id);
    }

    // Browse shows by movie and city on a specific date
    public List<Show> getShowsByMovieAndCity(Long movieId, City city, LocalDate date) {
        return showRepository.findByMovieIdAndTheatre_CityAndShowDate(movieId, city, date);
    }

    // Browse shows in a theatre on a specific date
    public List<Show> getShowsByTheatreAndDate(Theatre theatre, LocalDate date) {
        return showRepository.findByTheatreAndShowDate(theatre, date);
    }

    // Browse afternoon shows for a given date
    public List<Show> getAfternoonShows(LocalDate date) {
        return showRepository.findByShowDateAndStartTimeBetween(date, LocalTime.NOON, LocalTime.of(16, 0));
    }

    // Create or update a show
    public Show saveShow(Show show) {
        return showRepository.save(show);
    }

    // Delete a show by ID
    public void deleteShow(Long showId) {
        showRepository.deleteById(showId);
    }
}
