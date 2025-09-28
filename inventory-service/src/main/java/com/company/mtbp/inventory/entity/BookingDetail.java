package com.company.mtbp.inventory.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "booking_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    @ToString.Exclude
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    @ToString.Exclude
    private Seat seat;

    @ManyToOne
    @JoinColumn(name = "show_id")
    @ToString.Exclude
    private Show show;

    private Double price;

    private Double discountApplied; // percentage or amount

}
