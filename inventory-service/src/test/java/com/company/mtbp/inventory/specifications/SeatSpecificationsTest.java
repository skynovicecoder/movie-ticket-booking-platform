package com.company.mtbp.inventory.specifications;

import com.company.mtbp.inventory.entity.Seat;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings({"rawtypes", "unchecked"})
class SeatSpecificationsTest {

    @Test
    void byId_returnsPredicate() {
        Root<Seat> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        Path path = mock(Path.class);
        when(root.get("id")).thenReturn(path);

        Long seatId = 10L;
        Predicate predicate = mock(Predicate.class);
        when(cb.equal(path, seatId)).thenReturn(predicate);

        Predicate result = SeatSpecifications.byId(seatId).toPredicate(root, query, cb);
        assertEquals(predicate, result);

        // Null input should return null
        assertNull(SeatSpecifications.byId(null).toPredicate(root, query, cb));
    }

    @Test
    void byTheatreId_returnsPredicate() {
        Root<Seat> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        Path theatrePath = mock(Path.class);
        Path idPath = mock(Path.class);

        when(root.get("theatre")).thenReturn(theatrePath);
        when(theatrePath.get("id")).thenReturn(idPath);

        Long theatreId = 5L;
        Predicate predicate = mock(Predicate.class);
        when(cb.equal(idPath, theatreId)).thenReturn(predicate);

        Predicate result = SeatSpecifications.byTheatreId(theatreId).toPredicate(root, query, cb);
        assertEquals(predicate, result);

        assertNull(SeatSpecifications.byTheatreId(null).toPredicate(root, query, cb));
    }

    @Test
    void bySeatNumber_returnsPredicate() {
        Root<Seat> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        Path path = mock(Path.class);
        when(root.get("seatNumber")).thenReturn(path);

        String seatNumber = "A1";
        Predicate predicate = mock(Predicate.class);
        when(cb.equal(path, seatNumber)).thenReturn(predicate);

        Predicate result = SeatSpecifications.bySeatNumber(seatNumber).toPredicate(root, query, cb);
        assertEquals(predicate, result);

        assertNull(SeatSpecifications.bySeatNumber(null).toPredicate(root, query, cb));
    }

    @Test
    void bySeatType_returnsPredicate() {
        Root<Seat> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        Path path = mock(Path.class);
        when(root.get("seatType")).thenReturn(path);

        String seatType = "VIP";
        Predicate predicate = mock(Predicate.class);
        when(cb.equal(path, seatType)).thenReturn(predicate);

        Predicate result = SeatSpecifications.bySeatType(seatType).toPredicate(root, query, cb);
        assertEquals(predicate, result);

        assertNull(SeatSpecifications.bySeatType(null).toPredicate(root, query, cb));
    }

    @Test
    void byAvailability_returnsPredicate() {
        Root<Seat> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        Path path = mock(Path.class);
        when(root.get("available")).thenReturn(path);

        Boolean available = true;
        Predicate predicate = mock(Predicate.class);
        when(cb.equal(path, available)).thenReturn(predicate);

        Predicate result = SeatSpecifications.byAvailability(available).toPredicate(root, query, cb);
        assertEquals(predicate, result);

        assertNull(SeatSpecifications.byAvailability(null).toPredicate(root, query, cb));
    }
}
