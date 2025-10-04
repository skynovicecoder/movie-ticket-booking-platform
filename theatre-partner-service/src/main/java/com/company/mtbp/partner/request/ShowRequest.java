package com.company.mtbp.partner.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowRequest {
    private Long movieId;
    private Long theatreId;
    private LocalDate showDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Double pricePerTicket;
    private String showType;
}