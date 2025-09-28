package com.company.mtbp.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {
    private Long id;
    private String title;
    private String genre;
    private Integer durationMinutes;
    private String language;
    private LocalDate releaseDate;
    private String rating;
}
