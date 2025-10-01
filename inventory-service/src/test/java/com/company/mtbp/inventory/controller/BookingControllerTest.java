package com.company.mtbp.inventory.controller;

import com.company.mtbp.inventory.dto.BookingDTO;
import com.company.mtbp.inventory.dto.CustomerDTO;
import com.company.mtbp.inventory.dto.SeatSelectionRequest;
import com.company.mtbp.inventory.dto.ShowDTO;
import com.company.mtbp.inventory.exception.ResourceNotFoundException;
import com.company.mtbp.inventory.service.BookingService;
import com.company.mtbp.inventory.service.CustomerService;
import com.company.mtbp.inventory.service.ShowService;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@Slf4j
@ActiveProfiles("test")
class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @Mock
    private CustomerService customerService;

    @Mock
    private ShowService showService;

    @InjectMocks
    private BookingController bookingController;

    private MockMvc mockMvc;

    private CustomerDTO sampleCustomer;
    private ShowDTO sampleShow;
    private BookingDTO sampleBooking;

    @BeforeEach
    void setup() {
        Faker faker = new Faker();
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();

        sampleCustomer = new CustomerDTO();
        sampleCustomer.setId(1L);
        sampleCustomer.setName(faker.name().fullName());

        sampleShow = new ShowDTO();
        sampleShow.setId(1L);
        sampleShow.setMovieTitle("Interstellar");
        sampleShow.setShowDate(LocalDate.now());
        sampleShow.setStartTime(LocalTime.of(18, 0));

        sampleBooking = new BookingDTO();
        sampleBooking.setId(1L);
        sampleBooking.setCustomerId(sampleCustomer.getId());
        sampleBooking.setShowId(sampleShow.getId());
    }

    @Test
    void bookTickets_returnsCreatedBooking() throws Exception {
        SeatSelectionRequest request = new SeatSelectionRequest();
        request.setSeatIds(List.of(101L, 102L));

        Mockito.when(customerService.getCustomerById(1L)).thenReturn(Optional.of(sampleCustomer));
        Mockito.when(showService.getShowById(1L)).thenReturn(Optional.of(sampleShow));
        Mockito.when(bookingService.bookTickets(eq(sampleCustomer), eq(sampleShow), eq(request.getSeatIds())))
                .thenReturn(sampleBooking);

        mockMvc.perform(post("/api/v1/bookings")
                        .param("customerId", "1")
                        .param("showId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"seatIds\":[101,102]}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.customerId").value(1L))
                .andExpect(jsonPath("$.showId").value(1L));
    }

    @Test
    void bookTickets_throwsWhenSeatIdsMissing() {
        Long customerId = 1L;
        Long showId = 1L;

        SeatSelectionRequest request = new SeatSelectionRequest();
        request.setSeatIds(List.of());

        assertThrows(ResourceNotFoundException.class, () -> {
            bookingController.bookTickets(customerId, showId, request);
        });
    }

    @Test
    void bulkBookTickets_returnsCreatedBooking() throws Exception {
        Mockito.when(customerService.getCustomerById(1L)).thenReturn(Optional.of(sampleCustomer));
        Mockito.when(showService.getShowById(1L)).thenReturn(Optional.of(sampleShow));
        Mockito.when(bookingService.bulkBookTickets(eq(sampleCustomer), eq(sampleShow), eq(2)))
                .thenReturn(sampleBooking);

        mockMvc.perform(post("/api/v1/bookings/bulk")
                        .param("customerId", "1")
                        .param("showId", "1")
                        .param("numberOfTicketsReq", "2"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void bulkBookTickets_throwsWhenNumberOfTicketsMissing() {
        Long customerId = 1L;
        Long showId = 1L;
        int numberOfTicketsReq = 0;

        assertThrows(ResourceNotFoundException.class, () -> {
            bookingController.bulkBookTickets(customerId, showId, numberOfTicketsReq);
        });
    }

    @Test
    void cancelBooking_returnsCancelledBooking() throws Exception {
        Mockito.when(bookingService.cancelBooking(1L)).thenReturn(sampleBooking);

        mockMvc.perform(put("/api/v1/bookings/1/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void bookTickets_throwsWhenCustomerNotFound() {
        Long customerId = 99L;
        Long showId = 1L;

        Mockito.when(customerService.getCustomerById(customerId)).thenReturn(Optional.empty());

        SeatSelectionRequest request = new SeatSelectionRequest();
        request.setSeatIds(List.of(101L));

        assertThrows(ResourceNotFoundException.class, () -> {
            bookingController.bookTickets(customerId, showId, request);
        });
    }

    @Test
    void bookTickets_throwsWhenShowNotFound() {
        Mockito.when(customerService.getCustomerById(1L)).thenReturn(Optional.of(sampleCustomer));
        Mockito.when(showService.getShowById(99L)).thenReturn(Optional.empty());

        SeatSelectionRequest request = new SeatSelectionRequest();
        request.setSeatIds(List.of(101L));

        assertThrows(ResourceNotFoundException.class, () -> {
            bookingController.bookTickets(1L, 99L, request);
        });
    }
}
