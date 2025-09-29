package com.company.mtbp.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
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
