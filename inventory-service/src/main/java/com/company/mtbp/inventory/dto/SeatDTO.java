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
    private String seatNumber;   // e.g., A1, B2
    private String seatType;     // REGULAR, PREMIUM, VIP
    private Boolean available;

    private Long theatreId;
    private String theatreName;  // optional, if you want to show theatre name

    private Long showId;
}
