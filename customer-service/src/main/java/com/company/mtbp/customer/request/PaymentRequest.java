package com.company.mtbp.customer.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    @NotNull(message = "Booking ID cannot be null")
    private Long bookingId;
    @NotNull(message = "Amount cannot be null")
    private Double amount;
    @NotNull(message = "Payment Method cannot be null")
    private String paymentMethod; // Example, "CARD", "UPI"
}
