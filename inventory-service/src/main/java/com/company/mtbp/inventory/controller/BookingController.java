package com.company.mtbp.inventory.controller;

import com.company.mtbp.inventory.dto.BookingDTO;
import com.company.mtbp.inventory.dto.CustomerDTO;
import com.company.mtbp.inventory.dto.SeatSelectionRequest;
import com.company.mtbp.inventory.dto.ShowDTO;
import com.company.mtbp.inventory.exception.ResourceNotFoundException;
import com.company.mtbp.inventory.record.BookingContextValidation;
import com.company.mtbp.inventory.service.BookingService;
import com.company.mtbp.inventory.service.CustomerService;
import com.company.mtbp.inventory.service.ShowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<BookingDTO> bookTickets(@RequestParam(required = false) Long customerId,
                                                  @RequestParam(required = false) Long showId,
                                                  @RequestBody(required = false) SeatSelectionRequest request) {
        if (request == null || request.getSeatIds() == null || request.getSeatIds().isEmpty()) {
            throw new ResourceNotFoundException("Seats selection is mandatory");
        }

        BookingContextValidation bookingContext = validateCustomerAndShow(customerId, showId);

        BookingDTO bookingDto = bookingService.bookTickets(bookingContext.customer(), bookingContext.show(), request.getSeatIds());

        return ResponseEntity.status(HttpStatus.CREATED).body(bookingDto);
    }

    @PostMapping("/bulk")
    public ResponseEntity<BookingDTO> bulkBookTickets(@RequestParam(required = false) Long customerId,
                                                      @RequestParam(required = false) Long showId,
                                                      @RequestParam(defaultValue = "0") int numberOfTicketsReq) {
        if (numberOfTicketsReq == 0) {
            throw new ResourceNotFoundException("Required number of tickets missing");
        }

        BookingContextValidation bookingContext = validateCustomerAndShow(customerId, showId);

        BookingDTO bookingDto = bookingService.bulkBookTickets(bookingContext.customer(), bookingContext.show(), numberOfTicketsReq);

        return ResponseEntity.status(HttpStatus.CREATED).body(bookingDto);
    }

    private BookingContextValidation validateCustomerAndShow(Long customerId, Long showId) {
        if (customerId == null || customerId == 0) {
            throw new ResourceNotFoundException("Customer not found with ID: " + customerId);
        } else if (showId == null || showId == 0) {
            throw new ResourceNotFoundException("Show not found with ID: " + showId);
        }

        CustomerDTO customerDto = customerService.getCustomerById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + customerId));

        ShowDTO showDto = showService.getShowById(showId)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found with ID: " + showId));

        return new BookingContextValidation(customerDto, showDto);
    }
}
