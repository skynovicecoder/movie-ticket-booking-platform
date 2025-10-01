package com.company.mtbp.inventory.service;

import com.company.mtbp.inventory.dto.BookingDTO;
import com.company.mtbp.inventory.dto.CustomerDTO;
import com.company.mtbp.inventory.dto.ShowDTO;
import com.company.mtbp.inventory.entity.*;
import com.company.mtbp.inventory.events.InventoryCreatedEvent;
import com.company.mtbp.inventory.exception.BadRequestException;
import com.company.mtbp.inventory.exception.BookingAlreadyCancelledException;
import com.company.mtbp.inventory.exception.ResourceNotFoundException;
import com.company.mtbp.inventory.kafka.InventoryEventPublisher;
import com.company.mtbp.inventory.mapper.BookingMapper;
import com.company.mtbp.inventory.mapper.CustomerMapper;
import com.company.mtbp.inventory.mapper.InventoryCreatedEventMapper;
import com.company.mtbp.inventory.mapper.ShowMapper;
import com.company.mtbp.inventory.repository.BookingDetailRepository;
import com.company.mtbp.inventory.repository.BookingRepository;
import com.company.mtbp.inventory.repository.DiscountRulesRepository;
import com.company.mtbp.inventory.repository.SeatRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    BookingRepository bookingRepository;
    @Mock
    BookingDetailRepository bookingDetailRepository;
    @Mock
    SeatRepository seatRepository;
    @Mock
    CustomerMapper customerMapper;
    @Mock
    ShowMapper showMapper;
    @Mock
    BookingMapper bookingMapper;
    @Mock
    InventoryCreatedEventMapper inventoryCreatedEventMapper;
    @Mock
    private InventoryEventPublisher inventoryEventPublisher;

    @Mock
    ObjectMapper objectMapper;

    @Mock
    DiscountRulesRepository discountRulesRepository;

    @InjectMocks
    BookingService bookingService;

    CustomerDTO sampleCustomerDTO;
    Customer sampleCustomer;
    ShowDTO sampleShowDTO;
    Show sampleShow;
    Seat sampleSeat;
    Booking sampleBooking;
    BookingDTO sampleBookingDTO;

    @BeforeEach
    void setup() {
        sampleCustomerDTO = new CustomerDTO();
        sampleCustomerDTO.setId(1L);

        sampleCustomer = new Customer();
        sampleCustomer.setId(1L);

        sampleShowDTO = new ShowDTO();
        sampleShowDTO.setId(1L);
        sampleShowDTO.setPricePerTicket(100.0);
        sampleShowDTO.setStartTime(LocalTime.of(10, 0));

        sampleShow = new Show();
        sampleShow.setId(1L);
        sampleShow.setPricePerTicket(100.0);
        sampleShow.setStartTime(LocalTime.of(10, 0));

        sampleSeat = new Seat();
        sampleSeat.setId(101L);
        sampleSeat.setSeatNumber("A1");
        sampleSeat.setAvailable(true);

        sampleBooking = new Booking();
        sampleBooking.setId(1L);
        sampleBooking.setStatus("BOOKED");
        List<BookingDetail> bookingDtls = List.of(new BookingDetail(1L, sampleBooking, sampleSeat, sampleShow, 450.0, 50.0));
        sampleBooking.setBookingDetails(bookingDtls);

        sampleBookingDTO = new BookingDTO();
        sampleBookingDTO.setId(1L);
    }

    @Test
    void bookTickets_success() throws JsonProcessingException {
        when(customerMapper.toEntity(sampleCustomerDTO)).thenReturn(sampleCustomer);
        when(showMapper.toEntity(sampleShowDTO)).thenReturn(sampleShow);
        when(seatRepository.findAllById(List.of(101L))).thenReturn(List.of(sampleSeat));
        when(bookingRepository.save(any())).thenReturn(sampleBooking);
        when(bookingMapper.toDTO(sampleBooking)).thenReturn(sampleBookingDTO);
        when(discountRulesRepository.findByActiveTrue()).thenReturn(List.of());

        InventoryCreatedEvent sampleEvent = new InventoryCreatedEvent(
                sampleBooking.getId(),
                sampleCustomer.getId(),
                sampleShow.getId(),
                sampleBooking.getBookingTime(),
                sampleBooking.getTotalAmount(),
                sampleBooking.getStatus(),
                sampleBooking.getBookingDetails().stream().map(BookingDetail::getId).toList()
        );
        when(inventoryCreatedEventMapper.toEvent(sampleBookingDTO)).thenReturn(sampleEvent);
        when(objectMapper.writeValueAsString(sampleEvent)).thenReturn("{\"id\":1}");

        BookingDTO result = bookingService.bookTickets(sampleCustomerDTO, sampleShowDTO, List.of(101L));

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertFalse(sampleSeat.getAvailable());
        verify(bookingRepository, atLeastOnce()).save(any());
        verify(bookingDetailRepository).save(any());
        verify(inventoryEventPublisher).sendMessage("{\"id\":1}");
    }

    @Test
    void bookTickets_throwsWhenSeatUnavailable() {
        sampleSeat.setAvailable(false);
        when(seatRepository.findAllById(List.of(101L))).thenReturn(List.of(sampleSeat));

        assertThrows(BadRequestException.class, () ->
                bookingService.bookTickets(sampleCustomerDTO, sampleShowDTO, List.of(101L))
        );
    }

    @Test
    void bulkBookTickets_success() throws JsonProcessingException {
        when(customerMapper.toEntity(sampleCustomerDTO)).thenReturn(sampleCustomer);
        when(showMapper.toEntity(sampleShowDTO)).thenReturn(sampleShow);
        when(seatRepository.findByAvailableTrue()).thenReturn(List.of(sampleSeat));
        when(bookingRepository.save(any())).thenReturn(sampleBooking);
        when(bookingMapper.toDTO(sampleBooking)).thenReturn(sampleBookingDTO);
        when(discountRulesRepository.findByActiveTrue()).thenReturn(List.of());

        InventoryCreatedEvent sampleEvent = new InventoryCreatedEvent(
                sampleBooking.getId(),
                sampleCustomer.getId(),
                sampleShow.getId(),
                sampleBooking.getBookingTime(),
                sampleBooking.getTotalAmount(),
                sampleBooking.getStatus(),
                sampleBooking.getBookingDetails().stream().map(BookingDetail::getId).toList()
        );
        when(inventoryCreatedEventMapper.toEvent(sampleBookingDTO)).thenReturn(sampleEvent);
        when(objectMapper.writeValueAsString(sampleEvent)).thenReturn("{\"id\":1}");

        BookingDTO result = bookingService.bulkBookTickets(sampleCustomerDTO, sampleShowDTO, 1);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(bookingRepository, atLeastOnce()).save(any());
        verify(inventoryEventPublisher).sendMessage("{\"id\":1}");
    }

    @Test
    void bulkBookTickets_throwsWhenNotEnoughSeats() {
        when(seatRepository.findByAvailableTrue()).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class, () ->
                bookingService.bulkBookTickets(sampleCustomerDTO, sampleShowDTO, 1)
        );
    }

    @Test
    void cancelBooking_success() throws JsonProcessingException {
        sampleBooking.setBookingDetails(List.of(
                BookingDetail.builder().seat(sampleSeat).build()
        ));
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(sampleBooking));
        when(bookingMapper.toDTO(sampleBooking)).thenReturn(sampleBookingDTO);

        InventoryCreatedEvent sampleEvent = new InventoryCreatedEvent(
                sampleBooking.getId(),
                sampleCustomer.getId(),
                sampleShow.getId(),
                sampleBooking.getBookingTime(),
                sampleBooking.getTotalAmount(),
                sampleBooking.getStatus(),
                sampleBooking.getBookingDetails().stream().map(BookingDetail::getId).toList()
        );
        when(inventoryCreatedEventMapper.toEvent(sampleBookingDTO)).thenReturn(sampleEvent);
        when(objectMapper.writeValueAsString(sampleEvent)).thenReturn("{\"id\":1}");

        BookingDTO result = bookingService.cancelBooking(1L);

        assertEquals("BOOKED".equals(sampleBooking.getStatus()) ? "CANCELLED" : sampleBooking.getStatus(), sampleBooking.getStatus());
        assertTrue(sampleSeat.getAvailable());
        assertNotNull(result);
        verify(inventoryEventPublisher).sendMessage("{\"id\":1}");
    }

    @Test
    void cancelBooking_throwsWhenBookingNotFound() {
        when(bookingRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                bookingService.cancelBooking(99L)
        );
    }

    @Test
    void cancelBooking_throwsWhenAlreadyCancelled() {
        sampleBooking.setStatus("CANCELLED");
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(sampleBooking));

        assertThrows(BookingAlreadyCancelledException.class, () ->
                bookingService.cancelBooking(1L)
        );
    }
}
