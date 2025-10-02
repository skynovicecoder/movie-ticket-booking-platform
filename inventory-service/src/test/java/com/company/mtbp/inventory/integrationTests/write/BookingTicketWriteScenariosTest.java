package com.company.mtbp.inventory.integrationTests.write;

import com.company.mtbp.inventory.dto.CustomerDTO;
import com.company.mtbp.inventory.dto.SeatSelectionRequest;
import com.company.mtbp.inventory.dto.ShowDTO;
import com.company.mtbp.inventory.entity.Booking;
import com.company.mtbp.inventory.entity.DiscountRules;
import com.company.mtbp.inventory.entity.Seat;
import com.company.mtbp.inventory.enums.ConditionType;
import com.company.mtbp.inventory.enums.DiscountType;
import com.company.mtbp.inventory.mapper.CustomerMapper;
import com.company.mtbp.inventory.mapper.ShowMapper;
import com.company.mtbp.inventory.repository.BookingDetailRepository;
import com.company.mtbp.inventory.repository.BookingRepository;
import com.company.mtbp.inventory.repository.DiscountRulesRepository;
import com.company.mtbp.inventory.repository.SeatRepository;
import com.company.mtbp.inventory.service.BookingService;
import com.company.mtbp.inventory.service.CustomerService;
import com.company.mtbp.inventory.service.ShowService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@ActiveProfiles("test")
@EnabledIfSystemProperty(named = "env", matches = "dev")
public class BookingTicketWriteScenariosTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookingService bookingService;

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

    @Autowired
    private ShowMapper showMapper;

    @Autowired
    private CustomerMapper customerMapper;


    @BeforeEach
    void setup() {
    }

    @Test
    void bookTickets_third_ticket_50_percent_discount_success() throws Exception {
        long customerId = 1L;
        long showId = 1L;

        CustomerDTO customerDTO = CustomerDTO.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .build();

        ShowDTO showDTO = ShowDTO.builder()
                .id(1L)
                .movieId(1L)
                .movieTitle("Inception")
                .theatreId(1L)
                .theatreName("PVR")
                .showDate(LocalDate.now())
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(12, 0))
                .pricePerTicket(200.0)
                .showType("2D")
                .build();

        Mockito.when(customerService.getCustomerById(1L))
                .thenReturn(Optional.of(customerDTO));

        Mockito.when(showService.getShowById(1L))
                .thenReturn(Optional.of(showDTO));

        Seat seat1 = Seat.builder().id(1L).seatNumber("R1").available(true).build();
        Seat seat2 = Seat.builder().id(2L).seatNumber("R2").available(true).build();
        Seat seat3 = Seat.builder().id(3L).seatNumber("R3").available(true).build();
        List<Long> seatIds = List.of(1L, 2L, 3L);

        Mockito.when(seatRepository.findAllById(seatIds))
                .thenReturn(List.of(seat1, seat2, seat3));
        Mockito.when(seatRepository.saveAll(Mockito.anyList()))
                .thenReturn(List.of(seat1, seat2, seat3));

        DiscountRules discountRule = DiscountRules.builder()
                .id(1L)
                .discount_type(DiscountType.PERCENTAGE)
                .discount_value(50.0)
                .conditionType(ConditionType.TICKET_INDEX)
                .rule_condition("{\"index\":2}")
                .active(true)
                .build();

        Mockito.when(discountRulesRepository.findByActiveTrue())
                .thenReturn(List.of(discountRule));

        Booking booking = Booking.builder()
                .id(1L)
                .status("BOOKED")
                .totalAmount(600.0)
                .show(showMapper.toEntity(showDTO))
                .customer(customerMapper.toEntity(customerDTO))
                .build();

        Mockito.when(bookingRepository.save(Mockito.any(Booking.class)))
                .thenReturn(booking);

        Mockito.when(bookingDetailRepository.save(Mockito.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        SeatSelectionRequest request = new SeatSelectionRequest(seatIds);

        mockMvc.perform(post("/api/v1/bookings")
                        .param("customerId", Long.toString(customerId))
                        .param("showId", Long.toString(showId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.totalAmount").value(500));
    }

    @Test
    void bookTickets_afternoon_20_percent_discount_success() throws Exception {
        long customerId = 1L;
        long showId = 1L;

        CustomerDTO customerDTO = CustomerDTO.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .build();

        ShowDTO showDTO = ShowDTO.builder()
                .id(1L)
                .movieId(1L)
                .movieTitle("Inception")
                .theatreId(1L)
                .theatreName("PVR")
                .showDate(LocalDate.now())
                .startTime(LocalTime.of(13, 0))
                .endTime(LocalTime.of(15, 0))
                .pricePerTicket(200.0)
                .showType("2D")
                .build();

        Mockito.when(customerService.getCustomerById(1L))
                .thenReturn(Optional.of(customerDTO));

        Mockito.when(showService.getShowById(1L))
                .thenReturn(Optional.of(showDTO));

        Seat seat1 = Seat.builder().id(1L).seatNumber("R1").available(true).build();
        Seat seat2 = Seat.builder().id(2L).seatNumber("R2").available(true).build();
        Seat seat3 = Seat.builder().id(3L).seatNumber("R3").available(true).build();
        List<Long> seatIds = List.of(1L, 2L, 3L);

        Mockito.when(seatRepository.findAllById(seatIds))
                .thenReturn(List.of(seat1, seat2, seat3));
        Mockito.when(seatRepository.saveAll(Mockito.anyList()))
                .thenReturn(List.of(seat1, seat2, seat3));

        DiscountRules discountRule = DiscountRules.builder()
                .id(1L)
                .discount_type(DiscountType.PERCENTAGE)
                .discount_value(20.0)
                .conditionType(ConditionType.SHOW_TIME)
                .rule_condition("{\"from\":\"12:00\",\"to\":\"16:00\"}")
                .active(true)
                .build();

        Mockito.when(discountRulesRepository.findByActiveTrue())
                .thenReturn(List.of(discountRule));

        Booking booking = Booking.builder()
                .id(1L)
                .status("BOOKED")
                .totalAmount(600.0)
                .show(showMapper.toEntity(showDTO))
                .customer(customerMapper.toEntity(customerDTO))
                .build();

        Mockito.when(bookingRepository.save(Mockito.any(Booking.class)))
                .thenReturn(booking);

        Mockito.when(bookingDetailRepository.save(Mockito.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        SeatSelectionRequest request = new SeatSelectionRequest(seatIds);

        mockMvc.perform(post("/api/v1/bookings")
                        .param("customerId", Long.toString(customerId))
                        .param("showId", Long.toString(showId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.totalAmount").value(480));
    }

    @Test
    void bookTickets_noSeatsSelected_throwsException() throws Exception {
        SeatSelectionRequest emptyRequest = new SeatSelectionRequest(List.of());

        mockMvc.perform(post("/api/v1/bookings")
                        .param("customerId", "1")
                        .param("showId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emptyRequest)))
                .andExpect(status().isNotFound());
    }
}
