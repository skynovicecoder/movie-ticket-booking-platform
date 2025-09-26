package com.company.mtbp.inventory.controller;

import com.company.mtbp.inventory.entity.Booking;
import com.company.mtbp.inventory.entity.Customer;
import com.company.mtbp.inventory.entity.Show;
import com.company.mtbp.inventory.service.BookingService;
import com.company.mtbp.inventory.service.CustomerService;
import com.company.mtbp.inventory.service.ShowService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final CustomerService customerService;
    private final ShowService showService;

    public BookingController(BookingService bookingService, CustomerService customerService, ShowService showService) {
        this.bookingService = bookingService;
        this.customerService = customerService;
        this.showService = showService;
    }

    @PostMapping
    public Booking bookTickets(@RequestParam Long customerId,
                               @RequestParam Long showId,
                               @RequestBody List<Long> seatIds) {
        Customer customer = customerService.getCustomerById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Show show = showService.getShowById(showId)
                .orElseThrow(() -> new RuntimeException("Show not found"));

        return bookingService.bookTickets(customer, show, seatIds);
    }
}
