package com.company.mtbp.customer.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
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
