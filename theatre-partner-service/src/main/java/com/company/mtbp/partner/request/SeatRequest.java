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
public class SeatRequest {
    @NotNull(message = "Seat Number cannot be null")
    private String seatNumber;
    private String seatType;
    private boolean available;
    @NotNull(message = "Theatre ID cannot be null")
    private Long theatreId;
    private Long showId;
}