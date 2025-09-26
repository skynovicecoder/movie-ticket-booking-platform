package com.company.mtbp.inventory.repository;

import com.company.mtbp.inventory.entity.Booking;
import com.company.mtbp.inventory.entity.BookingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingDetailRepository extends JpaRepository<BookingDetail, Long> {

    List<BookingDetail> findByBooking(Booking booking);
}
