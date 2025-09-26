package com.company.mtbp.inventory.repository;

import com.company.mtbp.inventory.entity.Booking;
import com.company.mtbp.inventory.entity.Customer;
import com.company.mtbp.inventory.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByCustomer(Customer customer);

    List<Booking> findByShow(Show show);
}