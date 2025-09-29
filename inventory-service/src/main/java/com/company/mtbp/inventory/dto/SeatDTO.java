package com.company.mtbp.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatDTO {
    private Long id;
    private String seatNumber;   // Example: R1, P1, V1
    private String seatType;     // REGULAR, PREMIUM, VIP
    private Boolean available;

    private Long theatreId;
    private String theatreName;

    private Long showId;
}
