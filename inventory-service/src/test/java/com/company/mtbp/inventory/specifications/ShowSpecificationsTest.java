package com.company.mtbp.inventory.specifications;

import com.company.mtbp.inventory.entity.Movie;
import com.company.mtbp.inventory.entity.Show;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ShowSpecificationsTest {

    @Test
    void byMovie_returnsPredicate() {
        Root<Show> root = mock(Root.class);
        CriteriaQuery query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Movie movie = new Movie();

        Path moviePath = mock(Path.class);
        when(root.get("movie")).thenReturn(moviePath);

        Predicate predicate = mock(Predicate.class);
        when(cb.equal(moviePath, movie)).thenReturn(predicate);

        assertEquals(predicate, ShowSpecifications.byMovie(movie).toPredicate(root, query, cb));
        assertNull(ShowSpecifications.byMovie(null).toPredicate(root, query, cb));
    }

    @Test
    void byCity_returnsPredicate() {
        Root<Show> root = mock(Root.class);
        CriteriaQuery query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        String cityName = "Mumbai";

        Path theatrePath = mock(Path.class);
        Path cityPath = mock(Path.class);
        Path namePath = mock(Path.class);

        when(root.get("theatre")).thenReturn(theatrePath);
        when(theatrePath.get("city")).thenReturn(cityPath);
        when(cityPath.get("name")).thenReturn(namePath);

        Predicate predicate = mock(Predicate.class);
        when(cb.equal(namePath, cityName)).thenReturn(predicate);

        assertEquals(predicate, ShowSpecifications.byCity(cityName).toPredicate(root, query, cb));
        assertNull(ShowSpecifications.byCity(null).toPredicate(root, query, cb));
    }

    @Test
    void byDate_returnsPredicate() {
        Root<Show> root = mock(Root.class);
        CriteriaQuery query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        LocalDate date = LocalDate.now();

        Path showDatePath = mock(Path.class);
        when(root.get("showDate")).thenReturn(showDatePath);

        Predicate predicate = mock(Predicate.class);
        when(cb.equal(showDatePath, date)).thenReturn(predicate);

        assertEquals(predicate, ShowSpecifications.byDate(date).toPredicate(root, query, cb));
        assertNull(ShowSpecifications.byDate(null).toPredicate(root, query, cb));
    }
}
