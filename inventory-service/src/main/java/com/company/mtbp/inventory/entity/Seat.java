package com.company.mtbp.inventory.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String seatNumber; // Example R1, P1, V1

    private String seatType; // REGULAR, PREMIUM, VIP

    private Boolean available = true;

    @ManyToOne
    @JoinColumn(name = "theatre_id")
    @ToString.Exclude
    private Theatre theatre;

    @ManyToOne
    @JoinColumn(name = "show_id")
    @ToString.Exclude
    private Show show;
}
