package com.company.mtbp.inventory.repository;

import com.company.mtbp.inventory.entity.City;
import com.company.mtbp.inventory.entity.Movie;
import com.company.mtbp.inventory.entity.Show;
import com.company.mtbp.inventory.entity.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long>, JpaSpecificationExecutor<Show> {

    // Browse shows for a movie in a list of theatres on a given date
    List<Show> findByMovieAndTheatreInAndShowDate(Movie movie, List<Theatre> theatres, LocalDate showDate);

    // Browse shows for a theatre on a given date
    List<Show> findByTheatreAndShowDate(Theatre theatre, LocalDate showDate);

    // Browse shows by date and time range (e.g., afternoon shows)
    List<Show> findByShowDateAndStartTimeBetween(LocalDate showDate, LocalTime start, LocalTime end);

    // Browse shows by movie ID and city on a given date
    List<Show> findByMovieIdAndTheatre_CityAndShowDate(Long movieId, City city, LocalDate showDate);
}
