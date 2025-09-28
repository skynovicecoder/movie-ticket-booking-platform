package com.company.mtbp.inventory.specifications;

import com.company.mtbp.inventory.entity.Seat;
import org.springframework.data.jpa.domain.Specification;

public class SeatSpecifications {

    public static Specification<Seat> byId(Long seatId) {
        return (root, query, cb) -> {
            if (seatId == null) return null;
            return cb.equal(root.get("id"), seatId);
        };
    }

    public static Specification<Seat> byTheatreId(Long theatreId) {
        return (root, query, cb) -> {
            if (theatreId == null) return null;
            return cb.equal(root.get("theatre").get("id"), theatreId);
        };
    }

    public static Specification<Seat> bySeatNumber(String seatNumber) {
        return (root, query, cb) -> {
            if (seatNumber == null) return null;
            return cb.equal(root.get("seatNumber"), seatNumber);
        };
    }

    public static Specification<Seat> bySeatType(String seatType) {
        return (root, query, cb) -> {
            if (seatType == null) return null;
            return cb.equal(root.get("seatType"), seatType);
        };
    }

    public static Specification<Seat> byAvailability(Boolean available) {
        return (root, query, cb) -> {
            if (available == null) return null;
            return cb.equal(root.get("available"), available);
        };
    }
}
