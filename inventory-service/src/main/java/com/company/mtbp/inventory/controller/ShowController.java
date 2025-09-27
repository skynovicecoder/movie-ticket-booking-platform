package com.company.mtbp.inventory.controller;

import com.company.mtbp.inventory.entity.Movie;
import com.company.mtbp.inventory.entity.Show;
import com.company.mtbp.inventory.service.MovieService;
import com.company.mtbp.inventory.service.ShowService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shows")
public class ShowController {

    private final ShowService showService;
    private final MovieService movieService;

    public ShowController(ShowService showService, MovieService movieService) {
        this.showService = showService;
        this.movieService = movieService;
    }

    // Browse shows by movie, city, and date
    @GetMapping("/browse")
    public List<Show> browseShows(@RequestParam(required = false) String movieTitle,
                                  @RequestParam(required = false) String cityName,
                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Movie movie = null;
        if (movieTitle != null) {
            movie = movieService.getMovieByTitle(movieTitle)
                    .orElseThrow(() -> new RuntimeException("Movie not found"));
        }

        return showService.getShows(movie, cityName, date);
    }

    // Create a show (theatre admin)
    @PostMapping
    public Show createShow(@RequestBody Show show) {
        return showService.saveShow(show);
    }

    @PatchMapping("/update/{showId}")
    public Show patchShow(@PathVariable("showId") Long showId, @RequestBody Map<String, Object> updates) {
        Show existingShow = showService.getShowById(showId)
                .orElseThrow(() -> new RuntimeException("Show not found with id: " + showId));

        updates.forEach((key, value) -> {
            try {
                Field field = Show.class.getDeclaredField(key);
                field.setAccessible(true);

                // Handle conversion for LocalDate and LocalTime if needed
                if (field.getType().equals(LocalDate.class) && value instanceof String) {
                    field.set(existingShow, LocalDate.parse((String) value));
                } else if (field.getType().equals(LocalTime.class) && value instanceof String) {
                    field.set(existingShow, LocalTime.parse((String) value));
                } else {
                    field.set(existingShow, value);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException("Failed to update field: " + key, e);
            }
        });

        return showService.saveShow(existingShow);
    }

    // Delete show
    @DeleteMapping("/{showId}")
    public void deleteShow(@PathVariable("showId") Long showId) {
        showService.deleteShow(showId);
    }
}
