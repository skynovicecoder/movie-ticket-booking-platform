package com.company.mtbp.inventory.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @ToString.Exclude
    @JsonBackReference
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "show_id")
    @ToString.Exclude
    private Show show;

    private LocalDateTime bookingTime;

    private Double totalAmount;

    private String status; // BOOKED, CANCELLED

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<BookingDetail> bookingDetails;
}
