package com.company.mtbp.inventory.mapper;

import com.company.mtbp.inventory.dto.BookingDTO;
import com.company.mtbp.inventory.entity.Booking;
import com.company.mtbp.inventory.entity.BookingDetail;
import com.company.mtbp.inventory.entity.Customer;
import com.company.mtbp.inventory.entity.Show;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookingMapperTest {

    private BookingMapper bookingMapper;

    @BeforeEach
    void setup() {
        bookingMapper = Mappers.getMapper(BookingMapper.class);
    }

    @Test
    void toDTO_shouldMapBookingToBookingDTO() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");

        Show show = new Show();
        show.setId(10L);
        show.setShowDate(LocalDate.of(2025, 9, 30));
        show.setStartTime(LocalTime.of(18, 30));

        BookingDetail detail1 = new BookingDetail();
        detail1.setId(100L);
        BookingDetail detail2 = new BookingDetail();
        detail2.setId(101L);

        Booking booking = new Booking();
        booking.setId(50L);
        booking.setCustomer(customer);
        booking.setShow(show);
        booking.setBookingDetails(List.of(detail1, detail2));

        BookingDTO dto = bookingMapper.toDTO(booking);

        assertNotNull(dto);
        assertEquals(50L, dto.getId());
        assertEquals(1L, dto.getCustomerId());
        assertEquals("John Doe", dto.getCustomerName());
        assertEquals(10L, dto.getShowId());
        assertEquals(LocalDateTime.of(2025, 9, 30, 18, 30), dto.getShowDateTime());
        assertEquals(List.of(100L, 101L), dto.getBookingDetailIds());
    }

    @Test
    void toEntity_shouldMapBookingDTOToBooking() {
        BookingDTO dto = new BookingDTO();
        dto.setId(50L);
        dto.setCustomerId(1L);
        dto.setShowId(10L);

        Booking booking = bookingMapper.toEntity(dto);

        assertNotNull(booking);
        assertEquals(50L, booking.getId());
        assertNotNull(booking.getCustomer());
        assertEquals(1L, booking.getCustomer().getId());
        assertNotNull(booking.getShow());
        assertEquals(10L, booking.getShow().getId());
        // bookingDetails should be ignored
        assertNull(booking.getBookingDetails());
    }

    @Test
    void mapBookingDetails_shouldReturnIds() {
        BookingDetail detail1 = new BookingDetail();
        detail1.setId(100L);
        BookingDetail detail2 = new BookingDetail();
        detail2.setId(101L);

        List<Long> ids = bookingMapper.mapBookingDetails(List.of(detail1, detail2));

        assertEquals(List.of(100L, 101L), ids);
    }

    @Test
    void mapToDateTime_shouldReturnLocalDateTime() {
        LocalDate date = LocalDate.of(2025, 9, 30);
        LocalTime time = LocalTime.of(18, 30);

        assertEquals(LocalDateTime.of(2025, 9, 30, 18, 30), bookingMapper.mapToDateTime(date, time));
        assertNull(bookingMapper.mapToDateTime(null, time));
        assertNull(bookingMapper.mapToDateTime(date, null));
        assertNull(bookingMapper.mapToDateTime(null, null));
    }
}
