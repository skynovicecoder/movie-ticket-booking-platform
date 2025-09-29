package com.company.mtbp.inventory.service;

import com.company.mtbp.inventory.dto.MovieDTO;
import com.company.mtbp.inventory.dto.ShowDTO;
import com.company.mtbp.inventory.entity.Movie;
import com.company.mtbp.inventory.entity.Show;
import com.company.mtbp.inventory.entity.Theatre;
import com.company.mtbp.inventory.exception.ResourceNotFoundException;
import com.company.mtbp.inventory.mapper.ShowMapper;
import com.company.mtbp.inventory.repository.MovieRepository;
import com.company.mtbp.inventory.repository.ShowRepository;
import com.company.mtbp.inventory.repository.TheatreRepository;
import com.company.mtbp.inventory.specifications.ShowSpecifications;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class ShowService {

    private final ShowRepository showRepository;
    private final MovieRepository movieRepository;
    private final ShowMapper showMapper;
    private final TheatreRepository theatreRepository;
    private final SeatService seatService;

    public ShowService(ShowRepository showRepository, MovieRepository movieRepository, ShowMapper showMapper, TheatreRepository theatreRepository, SeatService seatService) {
        this.showRepository = showRepository;
        this.movieRepository = movieRepository;
        this.showMapper = showMapper;
        this.theatreRepository = theatreRepository;
        this.seatService = seatService;
    }

    @Transactional
    public ShowDTO saveShow(ShowDTO showDTO) {
        Show show = showMapper.toEntity(showDTO);

        if (showDTO.getMovieId() != null) {
            Movie movie = movieRepository.findById(showDTO.getMovieId())
                    .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + showDTO.getMovieId()));
            show.setMovie(movie);
        }

        if (showDTO.getTheatreId() != null) {
            Theatre theatre = theatreRepository.findById(showDTO.getTheatreId())
                    .orElseThrow(() -> new ResourceNotFoundException("Theatre not found with id: " + showDTO.getTheatreId()));
            show.setTheatre(theatre);
        }

        Show savedShow = showRepository.save(show);

        int numberOfRowsUpdated = seatService.updateShowForTheatre(showDTO.getTheatreId(), savedShow.getId());
        log.debug("Number Of Rows Updated : {}", numberOfRowsUpdated);

        return showMapper.toDTO(savedShow);
    }

    public ShowDTO patchShow(ShowDTO showDTO, Map<String, Object> updates) {
        updates.forEach((key, value) -> {
            try {
                Field field = ShowDTO.class.getDeclaredField(key);
                field.setAccessible(true);

                if (field.getType().equals(LocalDate.class) && value instanceof String) {
                    field.set(showDTO, LocalDate.parse((String) value));
                } else if (field.getType().equals(LocalTime.class) && value instanceof String) {
                    field.set(showDTO, LocalTime.parse((String) value));
                } else {
                    field.set(showDTO, value);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException("Failed to update field: " + key, e);
            }
        });

        return saveShow(showDTO);
    }

    public List<ShowDTO> getShows(MovieDTO movieDTO, String cityName, LocalDate date) {
        Movie movie = movieDTO != null ? movieRepository.findById(movieDTO.getId()).orElse(null) : null;

        Specification<Show> spec = ShowSpecifications.byMovie(movie)
                .and(ShowSpecifications.byCity(cityName))
                .and(ShowSpecifications.byDate(date));

        List<Show> shows = showRepository.findAll(spec);

        return showMapper.toDTOList(shows);
    }

    public Optional<ShowDTO> getShowById(Long id) {
        return showRepository.findById(id)
                .map(showMapper::toDTO);
    }

    public void deleteShow(Long showId) {
        showRepository.deleteById(showId);
    }
}
