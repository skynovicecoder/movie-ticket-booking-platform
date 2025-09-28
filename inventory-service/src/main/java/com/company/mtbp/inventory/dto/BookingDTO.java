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

    // Customer Info
    private Long customerId;
    private String customerName;   // optional, just for display

    // Show Info
    private Long showId;
    private LocalDateTime showDateTime; // optional: derived from Show.startTime + date

    private LocalDateTime bookingTime;

    private Double totalAmount;

    private String status;  // BOOKED, CANCELLED

    // List of booking detail IDs or seat numbers
    private List<Long> bookingDetailIds;
}
