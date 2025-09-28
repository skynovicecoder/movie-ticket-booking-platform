package com.company.mtbp.inventory.service;

import com.company.mtbp.inventory.dto.BookingDTO;
import com.company.mtbp.inventory.dto.CustomerDTO;
import com.company.mtbp.inventory.dto.ShowDTO;
import com.company.mtbp.inventory.entity.*;
import com.company.mtbp.inventory.exception.BadRequestException;
import com.company.mtbp.inventory.mapper.BookingMapper;
import com.company.mtbp.inventory.mapper.CustomerMapper;
import com.company.mtbp.inventory.mapper.ShowMapper;
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
    private final CustomerMapper customerMapper;
    private final ShowMapper showMapper;
    private final BookingMapper bookingMapper;

    public BookingService(BookingRepository bookingRepository,
                          BookingDetailRepository bookingDetailRepository,
                          SeatRepository seatRepository, CustomerMapper customerMapper, ShowMapper showMapper, BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.bookingDetailRepository = bookingDetailRepository;
        this.seatRepository = seatRepository;
        this.customerMapper = customerMapper;
        this.showMapper = showMapper;
        this.bookingMapper = bookingMapper;
    }

    /*
    @Transactional
    public BookingDTO bookTickets(CustomerDTO CustomerDto, ShowDTO showDto, List<Long> seatIds) {
        Customer customerObj = customerMapper.toEntity(CustomerDto);
        Show showObj = showMapper.toEntity(showDto);

        List<Seat> seats = seatRepository.findAllById(seatIds);

        for (Seat seatObj : seats) {
            if (!seatObj.getAvailable()) {
                throw new BadRequestException("Seat " + seatObj.getSeatNumber() + " is not available");
            }
        }

        seats.forEach(seat -> seat.setAvailable(false));
        seatRepository.saveAll(seats);

        double total = 0;
        for (int i = 0; i < seats.size(); i++) {
            double price = showObj.getPricePerTicket();
            double discount = 0;
            if (i == 2) discount += 0.5 * price;
            if (showObj.getStartTime().isAfter(LocalTime.NOON) && showObj.getStartTime().isBefore(LocalTime.of(16, 0))) {
                discount += 0.2 * price;
            }
            total += price - discount;
        }

        Booking booking = Booking.builder()
                .customer(customerObj)
                .show(showObj)
                .bookingTime(java.time.LocalDateTime.now())
                .status("BOOKED")
                .totalAmount(total)
                .build();
        booking = bookingRepository.save(booking);

        for (Seat seatObj : seats) {
            BookingDetail detail = BookingDetail.builder()
                    .booking(booking)
                    .seat(seatObj)
                    .price(showObj.getPricePerTicket())
                    .discountApplied(0.0)
                    .show(showObj)
                    .build();
            bookingDetailRepository.save(detail);
        }

        return bookingMapper.toDTO(booking);
    }*/
    @Transactional
    public BookingDTO bookTickets(CustomerDTO customerDto, ShowDTO showDto, List<Long> seatIds) {
        Customer customerObj = customerMapper.toEntity(customerDto);
        Show showObj = showMapper.toEntity(showDto);

        List<Seat> seats = seatRepository.findAllById(seatIds);

        for (Seat seatObj : seats) {
            if (!seatObj.getAvailable()) {
                throw new BadRequestException("Seat " + seatObj.getSeatNumber() + " is not available");
            }
        }

        seats.forEach(seat -> seat.setAvailable(false));
        seatRepository.saveAll(seats);

        Booking booking = Booking.builder()
                .customer(customerObj)
                .show(showObj)
                .bookingTime(java.time.LocalDateTime.now())
                .status("BOOKED")
                .totalAmount(0.0)
                .build();
        booking = bookingRepository.save(booking);

        double total = 0.0;
        int index = 0;

        for (Seat seatObj : seats) {
            double price = showObj.getPricePerTicket();
            double discount = 0.0;

            if (index == 2) {
                discount += 0.5 * price;
            }

            if (showObj.getStartTime().isAfter(LocalTime.NOON)
                    && showObj.getStartTime().isBefore(LocalTime.of(16, 0))) {
                discount += 0.2 * price;
            }

            double finalPrice = price - discount;
            total += finalPrice;

            BookingDetail detail = BookingDetail.builder()
                    .booking(booking)
                    .seat(seatObj)
                    .price(price)
                    .discountApplied(discount)
                    .show(showObj)
                    .build();

            bookingDetailRepository.save(detail);
            index++;
        }

        booking.setTotalAmount(total);
        bookingRepository.save(booking);

        return bookingMapper.toDTO(booking);
    }

}
