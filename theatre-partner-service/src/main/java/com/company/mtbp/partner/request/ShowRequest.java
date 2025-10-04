package com.company.mtbp.partner.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class ShowRequest {
    @NotNull(message = "Movie ID cannot be null")
    private Long movieId;
    @NotNull(message = "Theatre ID cannot be null")
    private Long theatreId;
    private LocalDate showDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Double pricePerTicket;
    private String showType;
}