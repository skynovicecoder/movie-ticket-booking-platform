package com.company.mtbp.inventory.repository;


import com.company.mtbp.inventory.entity.Seat;
import com.company.mtbp.inventory.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByShowAndAvailableTrue(Show show);

    List<Seat> findByShow(Show show);
}
