package com.company.mtbp.inventory.integrationTests.write;

import com.company.mtbp.inventory.dto.SeatDTO;
import com.company.mtbp.inventory.entity.Seat;
import com.company.mtbp.inventory.entity.Show;
import com.company.mtbp.inventory.entity.Theatre;
import com.company.mtbp.inventory.mapper.SeatMapper;
import com.company.mtbp.inventory.repository.SeatRepository;
import com.company.mtbp.inventory.repository.ShowRepository;
import com.company.mtbp.inventory.repository.TheatreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TheatreUpdateSeatInventoryWriteScenariosTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SeatRepository seatRepository;

    @MockitoBean
    private TheatreRepository theatreRepository;

    @MockitoBean
    private ShowRepository showRepository;

    @MockitoBean
    private SeatMapper seatMapper;

    private Theatre theatre;
    private Show show;
    private Seat seat;
    private SeatDTO seatDTO;

    @BeforeEach
    void setup() {
        theatre = Theatre.builder().id(1L).name("PVR").address("City Mall").totalSeats(100).build();
        show = Show.builder().id(10L).pricePerTicket(200.0).build();
        seat = Seat.builder().id(5L).seatNumber("A1").available(true).theatre(theatre).show(show).build();

        seatDTO = SeatDTO.builder()
                .id(5L)
                .seatNumber("A1")
                .available(true)
                .theatreId(1L)
                .showId(10L)
                .build();
    }

    @Test
    void addSeat_shouldReturnCreatedSeat() throws Exception {
        // mocks
        Mockito.when(theatreRepository.findById(1L)).thenReturn(Optional.of(theatre));
        Mockito.when(showRepository.findById(10L)).thenReturn(Optional.of(show));
        Mockito.when(seatMapper.toEntity(any(SeatDTO.class))).thenReturn(seat);
        Mockito.when(seatRepository.save(any(Seat.class))).thenReturn(seat);
        Mockito.when(seatMapper.toDTO(any(Seat.class))).thenReturn(seatDTO);

        String requestJson = """
                {
                  "id":5,
                  "seatNumber":"A1",
                  "available":true,
                  "theatreId":1,
                  "showId":10
                }
                """;

        mockMvc.perform(post("/api/v1/seats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.seatNumber").value("A1"))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    void deleteSeat_shouldReturnNoContent() throws Exception {
        Mockito.when(seatRepository.findById(5L)).thenReturn(Optional.of(seat));

        mockMvc.perform(delete("/api/v1/seats/theatre/seat")
                        .param("seatId", "5"))
                .andExpect(status().isNoContent());

        Mockito.verify(seatRepository, times(1)).delete(seat);
    }

    @Test
    void patchSeat_shouldReturnUpdatedSeat() throws Exception {
        // mock repo to return seat
        Mockito.when(seatRepository.findById(5L)).thenReturn(Optional.of(seat));
        Mockito.when(seatRepository.save(any(Seat.class))).thenAnswer(inv -> inv.getArgument(0));
        Mockito.when(seatMapper.toDTO(any(Seat.class))).thenReturn(
                SeatDTO.builder()
                        .id(5L)
                        .seatNumber("A1")
                        .available(false) // patched value
                        .theatreId(1L)
                        .showId(10L)
                        .build()
        );

        String patchJson = """
                { "available": false }
                """;

        mockMvc.perform(patch("/api/v1/seats/theatre/{theatreId}/seat/{seatId}", 1L, 5L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.available").value(false));
    }
}
