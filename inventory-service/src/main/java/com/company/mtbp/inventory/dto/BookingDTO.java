package com.company.mtbp.inventory.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDTO {

    private Long id;

    private Long customerId;
    private String customerName;

    private Long showId;
    private LocalDateTime showDateTime;

    private LocalDateTime bookingTime;

    private Double totalAmount;

    private String status;  // BOOKED, CANCELLED

    private List<Long> bookingDetailIds;
}
