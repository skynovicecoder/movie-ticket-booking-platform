package com.company.mtbp.inventory.service;

import com.company.mtbp.inventory.dto.MovieDTO;
import com.company.mtbp.inventory.entity.Movie;
import com.company.mtbp.inventory.exception.BadRequestException;
import com.company.mtbp.inventory.mapper.MovieMapper;
import com.company.mtbp.inventory.repository.MovieRepository;
import com.company.mtbp.shared.dto.PageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    public MovieService(MovieRepository movieRepository, MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }

    public MovieDTO saveMovie(MovieDTO movieDTO) {
        Movie movieEntity = movieMapper.toEntity(movieDTO);

        Movie savedMovie = movieRepository.save(movieEntity);

        return movieMapper.toDTO(savedMovie);
    }

    public MovieDTO patchMovie(MovieDTO movieDTO, Map<String, Object> updates) {
        updates.forEach((key, value) -> {
            try {
                Field field = MovieDTO.class.getDeclaredField(key);
                field.setAccessible(true);

                if (field.getType().equals(LocalDate.class) && value instanceof String) {
                    field.set(movieDTO, LocalDate.parse((String) value));
                } else if (field.getType().equals(Integer.class) && value instanceof Number) {
                    field.set(movieDTO, ((Number) value).intValue());
                } else {
                    field.set(movieDTO, value);
                }

            } catch (NoSuchFieldException | IllegalAccessException e) {
                log.warn("Invalid Movie field: {}", key);
                throw new BadRequestException("Invalid Movie field: " + key);
            }
        });

        return saveMovie(movieDTO);
    }

    public PageResponse<MovieDTO> getAllMovies(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Movie> moviePage = movieRepository.findAll(pageable);

        List<MovieDTO> movieDTOs = movieMapper.toDTOList(moviePage.getContent());

        return new PageResponse<>(
                movieDTOs,
                moviePage.getNumber(),
                moviePage.getSize(),
                moviePage.getTotalElements(),
                moviePage.getTotalPages(),
                moviePage.isLast()
        );
    }

    public Optional<MovieDTO> getMovieById(Long id) {
        return movieRepository.findById(id)
                .map(movieMapper::toDTO);
    }

    public Optional<MovieDTO> getMovieByTitle(String title) {
        return movieRepository.findByTitle(title)
                .map(movieMapper::toDTO);
    }
}