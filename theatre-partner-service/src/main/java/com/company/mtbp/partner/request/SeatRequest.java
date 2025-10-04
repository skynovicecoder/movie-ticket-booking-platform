package com.company.mtbp.partner.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatRequest {
    private String seatNumber;
    private String seatType;
    private boolean available;
    private Long theatreId;
    private Long showId;
}