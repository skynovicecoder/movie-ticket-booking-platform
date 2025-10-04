package com.company.mtbp.partner.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatUpdateRequest {
    private String seatNumber;
    private String seatType;
    private Boolean available;
}