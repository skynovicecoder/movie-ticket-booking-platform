package com.company.mtbp.inventory.dto;

import lombok.*;

@Getter
@Setter
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