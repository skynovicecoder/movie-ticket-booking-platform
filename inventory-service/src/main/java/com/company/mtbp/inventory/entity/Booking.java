package com.company.mtbp.inventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "show_id")
    private Show show;

    private LocalDateTime bookingTime;

    private Double totalAmount;

    private String status; // BOOKED, CANCELLED

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<BookingDetail> bookingDetails;
}
