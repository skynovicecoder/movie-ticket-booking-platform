package com.company.mtbp.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowDTO {
    private Long id;

    private Long movieId;
    private String movieTitle;

    private Long theatreId;
    private String theatreName;

    private LocalDate showDate;
    private LocalTime startTime;
    private LocalTime endTime;

    private Double pricePerTicket;
    private String showType;
}
