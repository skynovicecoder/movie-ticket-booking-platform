package com.company.mtbp.inventory.service;

import com.company.mtbp.inventory.dto.SeatDTO;
import com.company.mtbp.inventory.entity.Seat;
import com.company.mtbp.inventory.entity.Show;
import com.company.mtbp.inventory.entity.Theatre;
import com.company.mtbp.inventory.exception.ResourceNotFoundException;
import com.company.mtbp.inventory.mapper.SeatMapper;
import com.company.mtbp.inventory.pagedto.PageResponse;
import com.company.mtbp.inventory.repository.SeatRepository;
import com.company.mtbp.inventory.repository.ShowRepository;
import com.company.mtbp.inventory.repository.TheatreRepository;
import com.company.mtbp.inventory.specifications.SeatSpecifications;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SeatService {

    private final SeatRepository seatRepository;
    private final TheatreRepository theatreRepository;
    private final SeatMapper seatMapper;
    private final ShowRepository showRepository;

    public SeatService(SeatRepository seatRepository, TheatreRepository theatreRepository, SeatMapper seatMapper, ShowRepository showRepository) {
        this.seatRepository = seatRepository;
        this.theatreRepository = theatreRepository;
        this.seatMapper = seatMapper;
        this.showRepository = showRepository;
    }

    public SeatDTO addSeat(SeatDTO seatDTO) {
        Seat seat = seatMapper.toEntity(seatDTO);

        Theatre theatre = theatreRepository.findById(seatDTO.getTheatreId())
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found with id: " + seatDTO.getTheatreId()));
        seat.setTheatre(theatre);

        Show show = showRepository.findById(seatDTO.getShowId())
                .orElseThrow(() -> new ResourceNotFoundException("Show not found with id: " + seatDTO.getShowId()));
        seat.setShow(show);

        Seat savedSeat = seatRepository.save(seat);
        return seatMapper.toDTO(savedSeat);
    }

    public void deleteSeat(Long seatId, Long theatreId, String seatNumber) {
        Seat seat = null;
        if (seatId != null) {
            seat = seatRepository.findById(seatId)
                    .orElseThrow(() -> new ResourceNotFoundException("Seat not found with id: " + seatId));
        } else {
            seat = seatRepository.findByTheatreIdAndSeatNumber(theatreId, seatNumber)
                    .orElseThrow(() -> new ResourceNotFoundException("Seat Number: " + seatNumber + " :does not belong to theatre with id: " + theatreId));
        }
        seatRepository.delete(seat);
    }

    public SeatDTO patchSeat(Long theatreId, Long seatId, Map<String, Object> updates) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with id: " + seatId));

        if (!seat.getTheatre().getId().equals(theatreId)) {
            throw new ResourceNotFoundException("Seat does not belong to theatre with id: " + theatreId);
        }

        updates.forEach((key, value) -> {
            try {
                Field field = Seat.class.getDeclaredField(key);
                field.setAccessible(true);

                if (field.getType().equals(Boolean.class) && value instanceof Boolean) {
                    field.set(seat, value);
                } else if (field.getType().equals(String.class) && value instanceof String) {
                    field.set(seat, value);
                } else {
                    throw new IllegalArgumentException("Invalid value for field: " + key);
                }

            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new ResourceNotFoundException("Invalid field: " + key);
            }
        });

        Seat updatedSeat = seatRepository.save(seat);
        return seatMapper.toDTO(updatedSeat);
    }

    public PageResponse<SeatDTO> getSeats(Long seatId, Long theatreId, String seatNumber, int page, int size) {
        List<SeatDTO> filteredSeats;

        if (theatreId != null && seatId != null) {
            filteredSeats = Collections.singletonList(getSeatByIdAndTheatre(seatId, theatreId));
        } else if (theatreId != null && seatNumber != null) {
            filteredSeats = getSeatsByTheatreIdAndSeatNumber(theatreId, seatNumber);
        } else if (theatreId != null) {
            filteredSeats = getSeatsByTheatre(theatreId);
        } else if (seatId != null) {
            filteredSeats = Collections.singletonList(getSeatById(seatId));
        } else if (seatNumber != null) {
            filteredSeats = getSeatsBySeatNumber(seatNumber);
        } else {
            filteredSeats = getAllSeats();
        }

        int start = page * size;
        int end = Math.min(start + size, filteredSeats.size());
        List<SeatDTO> pagedList = (start > filteredSeats.size()) ? List.of() : filteredSeats.subList(start, end);

        int totalPages = (int) Math.ceil((double) filteredSeats.size() / size);

        return new PageResponse<>(
                pagedList,
                page,
                size,
                filteredSeats.size(),
                totalPages,
                page == totalPages - 1 || totalPages == 0
        );
    }

    public SeatDTO getSeatByIdAndTheatre(Long seatId, Long theatreId) {
        Seat seat = seatRepository.findByIdAndTheatreId(seatId, theatreId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Seat not found with ID " + seatId + " in Theatre " + theatreId));
        return seatMapper.toDTO(seat);
    }

    public List<SeatDTO> getSeatsByTheatre(Long theatreId) {
        List<Seat> seats = seatRepository.findByTheatreId(theatreId);
        return seatMapper.toDTOList(seats);
    }

    public SeatDTO getSeatById(Long seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with ID " + seatId));
        return seatMapper.toDTO(seat);
    }

    public List<SeatDTO> getAllSeats() {
        return seatMapper.toDTOList(seatRepository.findAll());
    }

    public List<SeatDTO> getSeatsByTheatreIdAndSeatNumber(Long theatreId, String seatNumber) {
        return seatMapper.toDTOList(seatRepository.findAll());
    }

    public List<SeatDTO> getSeatsBySeatNumber(String seatNumber) {
        List<Seat> seats = seatRepository.findBySeatNumber(seatNumber);
        return seatMapper.toDTOList(seats);
    }

    public List<SeatDTO> getSeatsByFilter(Map<String, Object> filters) {

        Specification<Seat> spec = null;

        if (filters.containsKey("seatId")) {
            Number seatIdNum = (Number) filters.get("seatId");
            Long seatId = seatIdNum.longValue();
            spec = SeatSpecifications.byId(seatId);
        }
        if (filters.containsKey("theatreId")) {
            Number theatreIdNum = (Number) filters.get("theatreId");
            Long theatreId = theatreIdNum.longValue();
            spec = (spec == null ? SeatSpecifications.byTheatreId(theatreId)
                    : spec.and(SeatSpecifications.byTheatreId((Long) filters.get("theatreId"))));
        }
        if (filters.containsKey("seatNumber")) {
            spec = (spec == null ? SeatSpecifications.bySeatNumber((String) filters.get("seatNumber"))
                    : spec.and(SeatSpecifications.bySeatNumber((String) filters.get("seatNumber"))));
        }
        if (filters.containsKey("seatType")) {
            spec = (spec == null ? SeatSpecifications.bySeatType((String) filters.get("seatType"))
                    : spec.and(SeatSpecifications.bySeatType((String) filters.get("seatType"))));
        }
        if (filters.containsKey("available")) {
            spec = (spec == null ? SeatSpecifications.byAvailability((Boolean) filters.get("available"))
                    : spec.and(SeatSpecifications.byAvailability((Boolean) filters.get("available"))));
        }

        List<Seat> seats = seatRepository.findAll(spec);
        return seatMapper.toDTOList(seats);
    }

    public int updateShowForTheatre(Long theatreId, Long showId) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new RuntimeException("Show not found"));
        int updatedRows = seatRepository.updateShowIdByTheatreId(theatreId, show);
        log.debug("Updated rows: {}", updatedRows);
        return updatedRows;
    }

}

