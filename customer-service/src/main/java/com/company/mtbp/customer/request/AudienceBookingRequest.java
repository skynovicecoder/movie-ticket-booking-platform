package com.company.mtbp.customer.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AudienceBookingRequest {
    @NotNull(message = "Customer ID cannot be null")
    private Long customerId;
    @NotNull(message = "Show ID cannot be null")
    private Long showId;
    @NotEmpty(message = "Seat IDs cannot be empty")
    private List<Long> seatIds;
}
