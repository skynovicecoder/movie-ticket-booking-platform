package com.company.mtbp.customer.controller;

import com.company.mtbp.customer.request.AudienceBookingRequest;
import com.company.mtbp.customer.request.PaymentRequest;
import com.company.mtbp.customer.service.BookingService;
import com.company.mtbp.customer.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/audience")
public class AudienceController {
    private final BookingService bookingService;
    private final PaymentService paymentService;

    public AudienceController(BookingService bookingService, PaymentService paymentService) {
        this.bookingService = bookingService;
        this.paymentService = paymentService;
    }

    @PostMapping("/book")
    public Mono<ResponseEntity<String>> bookTickets(@RequestBody AudienceBookingRequest request) {
        return bookingService.bookTickets(request)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    if (e.getMessage().contains("Client error")) {
                        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(e.getMessage()));
                    } else if (e.getMessage().contains("Server error")) {
                        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(e.getMessage()));
                    } else {
                        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Unexpected error: " + e.getMessage()));
                    }
                });
    }

    @PostMapping("/book-and-pay")
    public Mono<ResponseEntity<String>> bookAndPay(@Valid @RequestBody PaymentRequest paymentRequest) {
        return paymentService.makePayment(paymentRequest)
                .map(paymentResponse -> ResponseEntity.ok("Booking and payment successful!"))
                .onErrorResume(e ->
                        Mono.just(ResponseEntity.status(500)
                                .body("Error during booking/payment: " + e.getMessage()))
                );
    }
}
