package com.company.mtbp.partner.controller;

import com.company.mtbp.partner.request.SeatRequest;
import com.company.mtbp.partner.request.SeatUpdateRequest;
import com.company.mtbp.partner.request.ShowRequest;
import com.company.mtbp.partner.request.ShowUpdateRequest;
import com.company.mtbp.partner.service.TheatreSeatService;
import com.company.mtbp.partner.service.TheatreShowService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TheatrePartnerController.class)
class TheatrePartnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TheatreShowService theatreShowService;

    @MockitoBean
    private TheatreSeatService theatreSeatService;

    @Test
    void createShow_ShouldReturnCreatedStatus() throws Exception {
        ShowRequest request = new ShowRequest(101L, 10L, LocalDate.of(2025, 10, 5),
                LocalTime.of(18, 0), LocalTime.of(20, 30), 250.0, "IMAX");

        Mockito.when(theatreShowService.createShow(any(ShowRequest.class)))
                .thenReturn("Show created successfully");

        mockMvc.perform(post("/api/v1/theatre-partner/shows")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Show created successfully"));
    }

    @Test
    void updateShow_ShouldReturnOkStatus() throws Exception {
        ShowUpdateRequest request = new ShowUpdateRequest();
        request.setPricePerTicket(300.0);

        Mockito.when(theatreShowService.updateShow(eq(1L), any(ShowUpdateRequest.class)))
                .thenReturn("Show updated successfully");

        mockMvc.perform(patch("/api/v1/theatre-partner/shows/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Show updated successfully"));
    }

    @Test
    void deleteShow_ShouldReturnNoContentStatus() throws Exception {
        doNothing().when(theatreShowService).deleteShow(1L);

        mockMvc.perform(delete("/api/v1/theatre-partner/shows/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string("Show with ID 1 deleted successfully."));
    }

    @Test
    void addSeat_ShouldReturnCreatedStatus() throws Exception {
        SeatRequest request = new SeatRequest();
        request.setTheatreId(10L);
        request.setSeatNumber("A1");

        Mockito.when(theatreSeatService.addSeat(any(SeatRequest.class)))
                .thenReturn("Seat added successfully");

        mockMvc.perform(post("/api/v1/theatre-partner/shows/seats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Seat added successfully"));
    }

    @Test
    void updateSeat_ShouldReturnOkStatus() throws Exception {
        SeatUpdateRequest request = new SeatUpdateRequest();
        request.setSeatNumber("V1");

        Mockito.when(theatreSeatService.updateSeat(eq(10L), eq(1L), any(SeatUpdateRequest.class)))
                .thenReturn("Seat updated successfully");

        mockMvc.perform(patch("/api/v1/theatre-partner/shows/theatre/10/seat/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Seat updated successfully"));
    }
}
