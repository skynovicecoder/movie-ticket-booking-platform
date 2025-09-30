package com.company.mtbp.inventory.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String genre;

    private Integer durationMinutes;

    private String language;

    private LocalDate releaseDate;

    private String rating;

    @OneToMany(mappedBy = "movie")
    @ToString.Exclude
    private List<Show> shows;
}
