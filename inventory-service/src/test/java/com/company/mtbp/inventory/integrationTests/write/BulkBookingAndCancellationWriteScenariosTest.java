package com.company.mtbp.inventory.integrationTests.write;

import com.company.mtbp.inventory.dto.CustomerDTO;
import com.company.mtbp.inventory.dto.ShowDTO;
import com.company.mtbp.inventory.entity.*;
import com.company.mtbp.inventory.repository.BookingDetailRepository;
import com.company.mtbp.inventory.repository.BookingRepository;
import com.company.mtbp.inventory.repository.DiscountRulesRepository;
import com.company.mtbp.inventory.repository.SeatRepository;
import com.company.mtbp.inventory.service.CustomerService;
import com.company.mtbp.inventory.service.ShowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BulkBookingAndCancellationWriteScenariosTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookingRepository bookingRepository;
    @MockitoBean
    private BookingDetailRepository bookingDetailRepository;
    @MockitoBean
    private SeatRepository seatRepository;
    @MockitoBean
    private DiscountRulesRepository discountRulesRepository;
    @MockitoBean
    private CustomerService customerService;
    @MockitoBean
    private ShowService showService;

    private CustomerDTO customer;
    private ShowDTO show;
    private Seat seat;
    private Booking booking;

    @BeforeEach
    void setup() {
        customer = CustomerDTO.builder().id(1L).name("John Doe").email("john@example.com").build();
        show = ShowDTO.builder()
                .id(10L)
                .movieId(100L)
                .theatreId(200L)
                .showDate(LocalDate.now())
                .startTime(LocalTime.of(18, 30))
                .endTime(LocalTime.of(21, 0))
                .pricePerTicket(200.0)
                .build();

        seat = Seat.builder().id(5L).seatNumber("A1").available(true).build();

        booking = Booking.builder()
                .id(50L)
                .customer(Customer.builder().id(1L).name("John Doe").build())
                .show(Show.builder().id(10L).pricePerTicket(200.0).build())
                .status("BOOKED")
                .totalAmount(200.0)
                .build();
    }

    @Test
    void bulkBookTickets_shouldReturnCreatedBooking() throws Exception {
        Mockito.when(customerService.getCustomerById(1L)).thenReturn(Optional.of(customer));
        Mockito.when(showService.getShowById(10L)).thenReturn(Optional.of(show));
        Mockito.when(seatRepository.findByAvailableTrue()).thenReturn(List.of(seat));
        Mockito.when(seatRepository.findAllById(any())).thenReturn(List.of(seat));
        Mockito.when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        Mockito.when(discountRulesRepository.findByActiveTrue()).thenReturn(List.of());

        mockMvc.perform(post("/api/v1/bookings/bulk")
                        .param("customerId", "1")
                        .param("showId", "10")
                        .param("numberOfTicketsReq", "1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(50L))
                .andExpect(jsonPath("$.status").value("BOOKED"));
    }

    @Test
    void cancelBooking_shouldReturnCancelledBooking() throws Exception {
        Booking bookingActive = Booking.builder()
                .id(60L)
                .status("BOOKED")
                .bookingDetails(List.of(BookingDetail.builder()
                        .id(100L)
                        .seat(seat)
                        .booking(booking)
                        .build()))
                .build();

        Mockito.when(bookingRepository.findById(60L)).thenReturn(Optional.of(bookingActive));
        Mockito.when(bookingRepository.save(any(Booking.class))).thenAnswer(inv -> {
            Movie movie = new Movie();
            movie.setTitle("Avengers");
            Show newShow = new Show();
            newShow.setId(1L);
            newShow.setMovie(movie);
            Booking saved = inv.getArgument(0);
            saved.setStatus("CANCELLED");
            saved.setShow(newShow);
            return saved;
        });
        Mockito.when(seatRepository.save(any(Seat.class))).thenReturn(seat);

        mockMvc.perform(put("/api/v1/bookings/{bookingId}/cancel", 60L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }
}
