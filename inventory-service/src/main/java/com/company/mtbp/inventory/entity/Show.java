package com.company.mtbp.inventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "shows")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "theatre_id")
    private Theatre theatre;

    private LocalDate showDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private Double pricePerTicket;

    private String showType; // morning, afternoon, evening

    @OneToMany(mappedBy = "show")
    private List<Seat> seats;

    @OneToMany(mappedBy = "show")
    private List<BookingDetail> bookingDetails;
}
