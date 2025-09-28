package com.company.mtbp.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TheatreDTO {
    private Long id;
    private String name;
    private String address;
    private Integer totalSeats;

    private Long cityId;
    private String cityName;
}