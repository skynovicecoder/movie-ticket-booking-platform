package com.company.mtbp.inventory.repository;


import com.company.mtbp.inventory.entity.Seat;
import com.company.mtbp.inventory.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long>, JpaSpecificationExecutor<Seat> {

    List<Seat> findByShowAndAvailableTrue(Show show);

    List<Seat> findByShow(Show show);

    // Optional: simple convenience methods
    List<Seat> findByTheatreId(Long theatreId);

    List<Seat> findByTheatreIdAndId(Long theatreId, Long seatId);

    Optional<Seat> findById(Long seatId);

    Optional<Seat> findByIdAndTheatreId(Long seatId, Long theatreId);

    Optional<Seat> findByTheatreIdAndSeatNumber(Long theatreId, String seatNumber);

    List<Seat> findBySeatNumber(String seatNumber);
}
