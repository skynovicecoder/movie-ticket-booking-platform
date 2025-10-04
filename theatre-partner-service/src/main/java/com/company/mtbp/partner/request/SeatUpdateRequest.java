package com.company.mtbp.partner.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeatUpdateRequest {
    @NotNull(message = "Seat Number cannot be null")
    private String seatNumber;
    private String seatType;
    private Boolean available;
}