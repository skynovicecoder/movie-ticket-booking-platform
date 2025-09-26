package com.company.mtbp.inventory.controller;

import com.company.mtbp.inventory.entity.Movie;
import com.company.mtbp.inventory.entity.Show;
import com.company.mtbp.inventory.entity.Theatre;
import com.company.mtbp.inventory.service.MovieService;
import com.company.mtbp.inventory.service.ShowService;
import com.company.mtbp.inventory.service.TheatreService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/shows")
public class ShowController {

    private final ShowService showService;
    private final MovieService movieService;
    private final TheatreService theatreService;

    public ShowController(ShowService showService, MovieService movieService, TheatreService theatreService) {
        this.showService = showService;
        this.movieService = movieService;
        this.theatreService = theatreService;
    }

    // Browse shows by movie, city, and date
    @GetMapping("/browse")
    public List<Show> browseShows(@RequestParam String movieTitle,
                                  @RequestParam String cityName,
                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Movie movie = movieService.getMovieByTitle(movieTitle)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        List<Theatre> theatres = theatreService.getTheatresByCity(cityName);
        return showService.getShows(movie, theatres, date);
    }

    // Create a show (theatre admin)
    @PostMapping
    public Show createShow(@RequestBody Show show) {
        return showService.saveShow(show);
    }

    // Delete show
    @DeleteMapping("/{showId}")
    public void deleteShow(@PathVariable Long showId) {
        showService.deleteShow(showId);
    }
}
