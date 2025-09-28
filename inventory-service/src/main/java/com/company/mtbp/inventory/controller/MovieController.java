package com.company.mtbp.inventory.controller;

import com.company.mtbp.inventory.dto.MovieDTO;
import com.company.mtbp.inventory.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<MovieDTO> createMovie(@RequestBody MovieDTO movieDTO) {
        MovieDTO savedMovie = movieService.saveMovie(movieDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<MovieDTO> patchMovie(@PathVariable("id") Long id,
                                               @RequestBody Map<String, Object> updates) {

        Optional<MovieDTO> optionalMovie = movieService.getMovieById(id);
        if (optionalMovie.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        MovieDTO movieDto = optionalMovie.get();
        MovieDTO updatedMovie = movieService.patchMovie(movieDto, updates);

        return ResponseEntity.ok(updatedMovie);
    }

    @GetMapping
    public ResponseEntity<List<MovieDTO>> getAllMovies() {
        List<MovieDTO> movies = movieService.getAllMovies();
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable Long id) {
        return movieService.getMovieById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/by-title")
    public ResponseEntity<MovieDTO> getMovieByTitle(@RequestParam String title) {
        return movieService.getMovieByTitle(title)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
