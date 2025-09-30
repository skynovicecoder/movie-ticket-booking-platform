package com.company.mtbp.inventory.dto;

import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeatSelectionRequest {
    private List<Long> seatIds;
}
