package com.company.mtbp.inventory.service;

import com.company.mtbp.inventory.dto.TheatreDTO;
import com.company.mtbp.inventory.entity.City;
import com.company.mtbp.inventory.entity.Seat;
import com.company.mtbp.inventory.entity.Theatre;
import com.company.mtbp.inventory.exception.BadRequestException;
import com.company.mtbp.inventory.exception.ResourceNotFoundException;
import com.company.mtbp.inventory.mapper.TheatreMapper;
import com.company.mtbp.inventory.pagedto.PageResponse;
import com.company.mtbp.inventory.repository.CityRepository;
import com.company.mtbp.inventory.repository.SeatRepository;
import com.company.mtbp.inventory.repository.TheatreRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class TheatreService {

    private final TheatreRepository theatreRepository;
    private final CityRepository cityRepository;
    private final TheatreMapper theatreMapper;
    private final SeatRepository seatRepository;

    public TheatreService(TheatreRepository theatreRepository, CityRepository cityRepository, TheatreMapper theatreMapper, SeatRepository seatRepository) {
        this.theatreRepository = theatreRepository;
        this.cityRepository = cityRepository;
        this.theatreMapper = theatreMapper;
        this.seatRepository = seatRepository;
    }

    @Transactional
    public TheatreDTO saveTheatre(TheatreDTO theatreDTO) {
        City city = null;
        if (theatreDTO.getCityId() != null) {
            city = cityRepository.findById(theatreDTO.getCityId())
                    .orElseThrow(() -> new ResourceNotFoundException("City not found: " + theatreDTO.getCityName()));
        } else if (theatreDTO.getCityName() != null) {
            city = cityRepository.findByName(theatreDTO.getCityName())
                    .orElseThrow(() -> new ResourceNotFoundException("City not found: " + theatreDTO.getCityName()));
        }

        Theatre theatre = theatreMapper.toEntity(theatreDTO);
        theatre.setCity(city);

        Theatre savedTheatre = theatreRepository.save(theatre);

        List<Seat> seats = prepareSeatsForTheatre(savedTheatre, theatreDTO.getTotalSeats());
        seatRepository.saveAll(seats);

        return theatreMapper.toDTO(savedTheatre);
    }

    public List<Seat> prepareSeatsForTheatre(Theatre theatre, int totalSeats) {
        List<Seat> seats = new ArrayList<>();

        int vipCount = (int) Math.ceil(totalSeats * 0.10);
        int premiumCount = (int) Math.ceil(totalSeats * 0.40);
        int regularCount = totalSeats - vipCount - premiumCount;

        for (int i = 1; i <= vipCount; i++) {
            seats.add(new Seat(null, "V" + i, "VIP", true, theatre, null));
        }

        for (int i = 1; i <= premiumCount; i++) {
            seats.add(new Seat(null, "P" + i, "PREMIUM", true, theatre, null));
        }

        for (int i = 1; i <= regularCount; i++) {
            seats.add(new Seat(null, "R" + i, "REGULAR", true, theatre, null));
        }

        return seats;
    }

    public TheatreDTO patchTheatre(TheatreDTO theatreDTO, Map<String, Object> updates) {
        updates.forEach((key, value) -> {
            try {
                Field field = TheatreDTO.class.getDeclaredField(key);
                field.setAccessible(true);

                if (field.getType().equals(Integer.class) && value instanceof Number) {
                    field.set(theatreDTO, ((Number) value).intValue());
                } else {
                    field.set(theatreDTO, value);
                }

            } catch (NoSuchFieldException | IllegalAccessException e) {
                log.warn("Invalid Theatre field: {}", key);
                throw new BadRequestException("Invalid Theatre field: " + key);
            }
        });

        return saveTheatre(theatreDTO);
    }

    public PageResponse<TheatreDTO> getAllTheatres(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Theatre> theatrePage = theatreRepository.findAll(pageable);

        List<TheatreDTO> theatreDTOs = theatreMapper.toDTOList(theatrePage.getContent());

        return new PageResponse<>(
                theatreDTOs,
                theatrePage.getNumber(),
                theatrePage.getSize(),
                theatrePage.getTotalElements(),
                theatrePage.getTotalPages(),
                theatrePage.isLast()
        );
    }

    public Optional<TheatreDTO> getTheatreById(Long id) {
        return theatreRepository.findById(id)
                .map(theatreMapper::toDTO);
    }

    public List<TheatreDTO> getTheatresByCity(String cityName) {
        Optional<City> city = cityRepository.findByName(cityName);
        if (city.isEmpty()) return List.of();

        List<Theatre> theatres = theatreRepository.findByCity(city.get());
        return theatreMapper.toDTOList(theatres);
    }
}
