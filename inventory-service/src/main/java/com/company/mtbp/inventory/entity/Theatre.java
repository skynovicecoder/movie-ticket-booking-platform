package com.company.mtbp.inventory.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "theatres")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Theatre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String address;

    private Integer totalSeats;

    @ManyToOne
    @JoinColumn(name = "city_id")
    @JsonBackReference
    @ToString.Exclude
    private City city;

    @OneToMany(mappedBy = "theatre")
    @ToString.Exclude
    private List<Show> shows;

    @OneToMany(mappedBy = "theatre")
    @ToString.Exclude
    private List<Seat> seats;
}
