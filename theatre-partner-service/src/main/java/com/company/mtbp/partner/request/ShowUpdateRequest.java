package com.company.mtbp.partner.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowUpdateRequest {
    private LocalDate showDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Double pricePerTicket;
    private String showType;
}