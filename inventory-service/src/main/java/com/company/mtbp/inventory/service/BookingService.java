package com.company.mtbp.inventory.service;

import com.company.mtbp.inventory.entity.*;
import com.company.mtbp.inventory.repository.BookingDetailRepository;
import com.company.mtbp.inventory.repository.BookingRepository;
import com.company.mtbp.inventory.repository.SeatRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingDetailRepository bookingDetailRepository;
    private final SeatRepository seatRepository;

    public BookingService(BookingRepository bookingRepository,
                          BookingDetailRepository bookingDetailRepository,
                          SeatRepository seatRepository) {
        this.bookingRepository = bookingRepository;
        this.bookingDetailRepository = bookingDetailRepository;
        this.seatRepository = seatRepository;
    }

    @Transactional
    public Booking bookTickets(Customer customerObj, Show showObj, List<Long> seatIds) {
        // Fetch seats
        List<Seat> seats = seatRepository.findAllById(seatIds);

        // Check availability
        for (Seat seatObj : seats) {
            if (!seatObj.getAvailable()) {
                throw new RuntimeException("Seat " + seatObj.getSeatNumber() + " is not available");
            }
        }

        // Mark seats as booked
        seats.forEach(seat -> seat.setAvailable(false));
        seatRepository.saveAll(seats);

        // Calculate total & discounts
        double total = 0;
        for (int i = 0; i < seats.size(); i++) {
            double price = showObj.getPricePerTicket();
            double discount = 0;
            if (i == 2) discount += 0.5 * price; // third ticket 50%
            if (showObj.getStartTime().isAfter(LocalTime.NOON) && showObj.getStartTime().isBefore(LocalTime.of(16, 0))) {
                discount += 0.2 * price; // afternoon show 20%
            }
            total += price - discount;
        }

        // Create Booking
        Booking booking = Booking.builder()
                .customer(customerObj)
                .show(showObj)
                .bookingTime(java.time.LocalDateTime.now())
                .status("BOOKED")
                .totalAmount(total)
                .build();
        booking = bookingRepository.save(booking);

        // Create BookingDetails
        for (Seat seatObj : seats) {
            BookingDetail detail = BookingDetail.builder()
                    .booking(booking)
                    .seat(seatObj)
                    .price(showObj.getPricePerTicket())
                    .discountApplied(0.0) // optionally store applied discount
                    .build();
            bookingDetailRepository.save(detail);
        }

        return booking;
    }
}
