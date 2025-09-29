package com.company.mtbp.inventory.service;

import com.company.mtbp.inventory.dto.BookingDTO;
import com.company.mtbp.inventory.dto.CustomerDTO;
import com.company.mtbp.inventory.dto.ShowDTO;
import com.company.mtbp.inventory.entity.*;
import com.company.mtbp.inventory.enums.DiscountType;
import com.company.mtbp.inventory.exception.BadRequestException;
import com.company.mtbp.inventory.exception.BookingAlreadyCancelledException;
import com.company.mtbp.inventory.exception.ResourceNotFoundException;
import com.company.mtbp.inventory.mapper.BookingMapper;
import com.company.mtbp.inventory.mapper.CustomerMapper;
import com.company.mtbp.inventory.mapper.ShowMapper;
import com.company.mtbp.inventory.repository.BookingDetailRepository;
import com.company.mtbp.inventory.repository.BookingRepository;
import com.company.mtbp.inventory.repository.DiscountRulesRepository;
import com.company.mtbp.inventory.repository.SeatRepository;
import com.company.mtbp.inventory.utils.JsonUtils;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingDetailRepository bookingDetailRepository;
    private final SeatRepository seatRepository;
    private final CustomerMapper customerMapper;
    private final ShowMapper showMapper;
    private final BookingMapper bookingMapper;
    private final DiscountRulesRepository discountRulesRepository;

    public BookingService(BookingRepository bookingRepository,
                          BookingDetailRepository bookingDetailRepository,
                          SeatRepository seatRepository, CustomerMapper customerMapper, ShowMapper showMapper, BookingMapper bookingMapper, DiscountRulesRepository discountRulesRepository) {
        this.bookingRepository = bookingRepository;
        this.bookingDetailRepository = bookingDetailRepository;
        this.seatRepository = seatRepository;
        this.customerMapper = customerMapper;
        this.showMapper = showMapper;
        this.bookingMapper = bookingMapper;
        this.discountRulesRepository = discountRulesRepository;
    }

    @Transactional
    public BookingDTO bookTickets(CustomerDTO customerDto, ShowDTO showDto, List<Long> seatIds) {
        Customer customerObj = customerMapper.toEntity(customerDto);
        Show showObj = showMapper.toEntity(showDto);

        List<Seat> seats = validateAndMarkSeats(seatIds);

        Booking booking = createBooking(customerObj, showObj);

        double total = applyDiscountsAndSaveDetails(booking, showObj, seats);

        booking.setTotalAmount(total);
        bookingRepository.save(booking);

        return bookingMapper.toDTO(booking);
    }

    @Transactional
    public BookingDTO bulkBookTickets(CustomerDTO customerDto, ShowDTO showDto, int numberOfTicketsReq) {
        Customer customerObj = customerMapper.toEntity(customerDto);
        Show showObj = showMapper.toEntity(showDto);

        List<Long> seatIds = getSeatIds(numberOfTicketsReq);

        List<Seat> seats = validateAndMarkSeats(seatIds);

        Booking booking = createBooking(customerObj, showObj);

        double total = applyDiscountsAndSaveDetails(booking, showObj, seats);

        booking.setTotalAmount(total);
        bookingRepository.save(booking);

        return bookingMapper.toDTO(booking);
    }

    @Transactional
    public BookingDTO cancelBooking(Long bookingId) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);

        if (optionalBooking.isEmpty()) {
            throw new ResourceNotFoundException("Booking not found with ID: " + bookingId);
        }

        Booking booking = optionalBooking.get();

        if ("CANCELLED".equalsIgnoreCase(booking.getStatus())) {
            throw new BookingAlreadyCancelledException("Booking is already cancelled for booking ID: " + bookingId);
        }

        booking.setStatus("CANCELLED");

        releaseAllSeats(booking.getBookingDetails());

        bookingRepository.save(booking);

        return bookingMapper.toDTO(booking);
    }

    private void releaseAllSeats(List<BookingDetail> bookingDetails) {
        for (BookingDetail detail : bookingDetails) {
            Seat seat = detail.getSeat();
            seat.setAvailable(true);
            seatRepository.save(seat);
        }
    }

    private List<Seat> validateAndMarkSeats(List<Long> seatIds) {
        List<Seat> seats = seatRepository.findAllById(seatIds);

        for (Seat seatObj : seats) {
            if (!seatObj.getAvailable()) {
                throw new BadRequestException("Seat " + seatObj.getSeatNumber() + " is not available");
            }
        }

        seats.forEach(seat -> seat.setAvailable(false));
        seatRepository.saveAll(seats);

        return seats;
    }

    private Booking createBooking(Customer customer, Show show) {
        Booking booking = Booking.builder()
                .customer(customer)
                .show(show)
                .bookingTime(java.time.LocalDateTime.now())
                .status("BOOKED")
                .totalAmount(0.0)
                .build();
        return bookingRepository.save(booking);
    }

    private double applyDiscountsAndSaveDetails(Booking booking, Show showObj, List<Seat> seats) {
        List<DiscountRules> rules = discountRulesRepository.findByActiveTrue();
        double total = 0.0;

        for (int i = 0; i < seats.size(); i++) {
            Seat seatObj = seats.get(i);
            double price = showObj.getPricePerTicket();
            double discount = 0.0;

            for (DiscountRules rule : rules) {
                switch (rule.getConditionType()) {
                    case TICKET_INDEX -> {
                        Map<String, Integer> cond = JsonUtils.parseToIntMap(rule.getRule_condition());
                        int index = cond.get("index");
                        if (i == index) {
                            discount += applyDiscount(rule, price);
                        }
                    }
                    case SHOW_TIME -> {
                        Map<String, String> cond = JsonUtils.parseToStringMap(rule.getRule_condition());
                        LocalTime from = LocalTime.parse(cond.get("from"));
                        LocalTime to = LocalTime.parse(cond.get("to"));
                        if (showObj.getStartTime().isAfter(from) && showObj.getStartTime().isBefore(to)) {
                            discount += applyDiscount(rule, price);
                        }
                    }
                }
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
        }

        return total;
    }

    private double applyDiscount(DiscountRules rule, double price) {
        if (rule.getDiscount_type() == DiscountType.PERCENTAGE) return price * rule.getDiscount_value() / 100.0;
        else return rule.getDiscount_value();
    }

    private List<Long> getSeatIds(int numberOfTicketsReq) {
        List<Seat> availableSeats = seatRepository.findByAvailableTrue();

        if (availableSeats == null || availableSeats.isEmpty()) {
            throw new ResourceNotFoundException("No available seats found");
        }

        if (availableSeats.size() < numberOfTicketsReq) {
            throw new ResourceNotFoundException(
                    "Only " + availableSeats.size() + " seats are available, but " + numberOfTicketsReq + " were requested"
            );
        }

        return availableSeats.stream()
                .limit(numberOfTicketsReq)
                .map(Seat::getId)
                .toList();
    }

}
