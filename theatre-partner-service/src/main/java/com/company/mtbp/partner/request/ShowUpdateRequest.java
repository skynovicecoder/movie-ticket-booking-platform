package com.company.mtbp.partner.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShowUpdateRequest {
    private LocalDate showDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Double pricePerTicket;
    private String showType;
}