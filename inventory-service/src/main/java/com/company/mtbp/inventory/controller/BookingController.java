package com.company.mtbp.inventory.controller;

import com.company.mtbp.inventory.dto.BookingDTO;
import com.company.mtbp.inventory.dto.CustomerDTO;
import com.company.mtbp.inventory.dto.ShowDTO;
import com.company.mtbp.inventory.exception.ResourceNotFoundException;
import com.company.mtbp.inventory.service.BookingService;
import com.company.mtbp.inventory.service.CustomerService;
import com.company.mtbp.inventory.service.ShowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<BookingDTO> bookTickets(@RequestParam Long customerId,
                                                  @RequestParam Long showId,
                                                  @RequestBody List<Long> seatIds) {
        if (seatIds == null || seatIds.isEmpty()) {
            throw new ResourceNotFoundException("Seats selection is mandatory");
        }

        CustomerDTO customerDto = customerService.getCustomerById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + customerId));

        ShowDTO showDto = showService.getShowById(showId)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found with ID: " + showId));

        BookingDTO bookingDto = bookingService.bookTickets(customerDto, showDto, seatIds);

        return ResponseEntity.status(HttpStatus.CREATED).body(bookingDto);
    }
}
