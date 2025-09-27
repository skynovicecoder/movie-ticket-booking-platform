package com.company.mtbp.inventory.controller;

import com.company.mtbp.inventory.entity.Movie;
import com.company.mtbp.inventory.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    // Create a new movie
    @PostMapping
    public Movie createMovie(@RequestBody Movie movie) {
        return movieService.saveMovie(movie);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Movie> patchMovie(@PathVariable("id") Long id,
                                            @RequestBody Map<String, Object> updates) {

        Optional<Movie> optionalMovie = movieService.getMovieById(id);
        if (optionalMovie.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Movie movie = optionalMovie.get();

        updates.forEach((key, value) -> {
            try {
                Field field = Movie.class.getDeclaredField(key);
                field.setAccessible(true);

                // Handle special types if needed
                if (field.getType().equals(LocalDate.class) && value instanceof String) {
                    field.set(movie, LocalDate.parse((String) value));
                } else if (field.getType().equals(Integer.class) && value instanceof Number) {
                    field.set(movie, ((Number) value).intValue());
                } else {
                    field.set(movie, value);
                }

            } catch (NoSuchFieldException | IllegalAccessException e) {
                // Ignore invalid fields or log warning
                System.out.println("Invalid field: " + key);
            }
        });

        Movie updatedMovie = movieService.saveMovie(movie);
        return ResponseEntity.ok(updatedMovie);
    }

    // Get all movies
    @GetMapping
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    // Get movie by ID
    @GetMapping("/{id}")
    public Movie getMovieById(@PathVariable("id") Long id) {
        return movieService.getMovieById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
    }
}
