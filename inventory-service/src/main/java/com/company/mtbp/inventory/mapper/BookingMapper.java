package com.company.mtbp.inventory.mapper;

import com.company.mtbp.inventory.dto.BookingDTO;
import com.company.mtbp.inventory.entity.Booking;
import com.company.mtbp.inventory.entity.BookingDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.name", target = "customerName")
    @Mapping(source = "show.id", target = "showId")
    @Mapping(target = "showDateTime", expression = "java(mapToDateTime(booking.getShow().getShowDate(), booking.getShow().getStartTime()))")
    @Mapping(source = "bookingDetails", target = "bookingDetailIds")
    BookingDTO toDTO(Booking booking);

    @Mapping(source = "customerId", target = "customer.id")
    @Mapping(source = "showId", target = "show.id")
    @Mapping(target = "bookingDetails", ignore = true)
    Booking toEntity(BookingDTO dto);

    List<BookingDTO> toDTOList(List<Booking> bookings);

    default List<Long> mapBookingDetails(List<BookingDetail> details) {
        if (details == null) return null;
        return details.stream()
                .map(BookingDetail::getId)
                .toList();
    }

    default LocalDateTime mapToDateTime(LocalDate date, LocalTime time) {
        return date != null && time != null ? date.atTime(time) : null;
    }
}
