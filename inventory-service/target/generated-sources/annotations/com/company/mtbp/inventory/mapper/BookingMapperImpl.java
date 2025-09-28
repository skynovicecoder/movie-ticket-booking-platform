package com.company.mtbp.inventory.mapper;

import com.company.mtbp.inventory.dto.BookingDTO;
import com.company.mtbp.inventory.entity.Booking;
import com.company.mtbp.inventory.entity.Customer;
import com.company.mtbp.inventory.entity.Show;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-28T20:32:13+0530",
    comments = "version: 1.6.0.Beta2, compiler: javac, environment: Java 21 (Eclipse Adoptium)"
)
@Component
public class BookingMapperImpl implements BookingMapper {

    @Override
    public BookingDTO toDTO(Booking booking) {
        if ( booking == null ) {
            return null;
        }

        BookingDTO.BookingDTOBuilder bookingDTO = BookingDTO.builder();

        bookingDTO.customerId( bookingCustomerId( booking ) );
        bookingDTO.customerName( bookingCustomerName( booking ) );
        bookingDTO.showId( bookingShowId( booking ) );
        bookingDTO.bookingDetailIds( mapBookingDetails( booking.getBookingDetails() ) );
        bookingDTO.id( booking.getId() );
        bookingDTO.bookingTime( booking.getBookingTime() );
        bookingDTO.totalAmount( booking.getTotalAmount() );
        bookingDTO.status( booking.getStatus() );

        bookingDTO.showDateTime( mapToDateTime(booking.getShow().getShowDate(), booking.getShow().getStartTime()) );

        return bookingDTO.build();
    }

    @Override
    public Booking toEntity(BookingDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Booking.BookingBuilder booking = Booking.builder();

        booking.customer( bookingDTOToCustomer( dto ) );
        booking.show( bookingDTOToShow( dto ) );
        booking.id( dto.getId() );
        booking.bookingTime( dto.getBookingTime() );
        booking.totalAmount( dto.getTotalAmount() );
        booking.status( dto.getStatus() );

        return booking.build();
    }

    @Override
    public List<BookingDTO> toDTOList(List<Booking> bookings) {
        if ( bookings == null ) {
            return null;
        }

        List<BookingDTO> list = new ArrayList<BookingDTO>( bookings.size() );
        for ( Booking booking : bookings ) {
            list.add( toDTO( booking ) );
        }

        return list;
    }

    private Long bookingCustomerId(Booking booking) {
        Customer customer = booking.getCustomer();
        if ( customer == null ) {
            return null;
        }
        return customer.getId();
    }

    private String bookingCustomerName(Booking booking) {
        Customer customer = booking.getCustomer();
        if ( customer == null ) {
            return null;
        }
        return customer.getName();
    }

    private Long bookingShowId(Booking booking) {
        Show show = booking.getShow();
        if ( show == null ) {
            return null;
        }
        return show.getId();
    }

    protected Customer bookingDTOToCustomer(BookingDTO bookingDTO) {
        if ( bookingDTO == null ) {
            return null;
        }

        Customer customer = new Customer();

        customer.setId( bookingDTO.getCustomerId() );

        return customer;
    }

    protected Show bookingDTOToShow(BookingDTO bookingDTO) {
        if ( bookingDTO == null ) {
            return null;
        }

        Show show = new Show();

        show.setId( bookingDTO.getShowId() );

        return show;
    }
}
