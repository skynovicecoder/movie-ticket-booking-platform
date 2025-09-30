package com.company.mtbp.inventory.controller;

import com.company.mtbp.inventory.dto.SeatDTO;
import com.company.mtbp.inventory.service.SeatService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class SeatControllerTest {

    @Mock
    private SeatService seatService;

    @InjectMocks
    private SeatController seatController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private SeatDTO sampleSeat;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(seatController).build();
        objectMapper = new ObjectMapper();

        sampleSeat = new SeatDTO();
        sampleSeat.setId(1L);
        sampleSeat.setSeatNumber("A1");
        sampleSeat.setTheatreId(10L);
        sampleSeat.setAvailable(true);
    }

    @Test
    void addSeat_returnsCreatedSeat() throws Exception {
        Mockito.when(seatService.addSeat(any(SeatDTO.class))).thenReturn(sampleSeat);

        mockMvc.perform(post("/api/seats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleSeat)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(sampleSeat.getId()))
                .andExpect(jsonPath("$.seatNumber").value(sampleSeat.getSeatNumber()))
                .andExpect(jsonPath("$.theatreId").value(sampleSeat.getTheatreId()));
    }

    @Test
    void deleteSeat_returnsNoContent() throws Exception {
        Mockito.doNothing().when(seatService).deleteSeat(1L, 10L, "A1");

        mockMvc.perform(delete("/api/seats/theatre/seat")
                        .param("seatId", "1")
                        .param("theatreId", "10")
                        .param("seatNumber", "A1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void patchSeat_updatesSeat() throws Exception {
        SeatDTO updatedSeat = new SeatDTO();
        updatedSeat.setId(1L);
        updatedSeat.setSeatNumber("A1");
        updatedSeat.setTheatreId(10L);
        updatedSeat.setAvailable(false);

        Mockito.when(seatService.patchSeat(eq(10L), eq(1L), any(Map.class)))
                .thenReturn(updatedSeat);

        mockMvc.perform(patch("/api/seats/theatre/10/seat/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"available\":false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(false));
    }

    @Test
    void getSeats_returnsSeats() throws Exception {
        Mockito.when(seatService.getSeats(1L, 10L, "A1")).thenReturn(List.of(sampleSeat));

        mockMvc.perform(get("/api/seats/theatre")
                        .param("seatId", "1")
                        .param("theatreId", "10")
                        .param("seatNumber", "A1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].seatNumber").value("A1"));
    }

    @Test
    void searchSeats_returnsFilteredSeats() throws Exception {
        Mockito.when(seatService.getSeatsByFilter(any(Map.class)))
                .thenReturn(List.of(sampleSeat));

        mockMvc.perform(post("/api/seats/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"theatreId\":10}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].theatreId").value(10));
    }
}
