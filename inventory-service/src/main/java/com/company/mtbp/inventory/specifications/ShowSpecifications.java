package com.company.mtbp.inventory.specifications;

import com.company.mtbp.inventory.entity.City;
import com.company.mtbp.inventory.entity.Movie;
import com.company.mtbp.inventory.entity.Show;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ShowSpecifications {

    public static Specification<Show> byMovie(Movie movie) {
        return (root, query, criteriaBuilder) -> {
            if (movie == null) return null; // null means no filtering
            return criteriaBuilder.equal(root.get("movie"), movie);
        };
    }

    public static Specification<Show> byCity(String cityName) {
        return (root, query, criteriaBuilder) -> {
            if (cityName == null) return null;
            return criteriaBuilder.equal(root.get("theatre").get("city").get("name"), cityName);
        };
    }

    public static Specification<Show> byDate(LocalDate date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) return null;
            return criteriaBuilder.equal(root.get("showDate"), date);
        };
    }
}
