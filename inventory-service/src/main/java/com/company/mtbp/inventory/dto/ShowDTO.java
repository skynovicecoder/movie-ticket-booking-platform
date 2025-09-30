package com.company.mtbp.inventory.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
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
