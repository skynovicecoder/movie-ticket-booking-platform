package com.company.mtbp.inventory.controller;

import com.company.mtbp.inventory.dto.TheatreDTO;
import com.company.mtbp.inventory.service.TheatreService;
import com.company.mtbp.shared.dto.PageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class TheatreControllerTest {

    @Mock
    private TheatreService theatreService;

    @InjectMocks
    private TheatreController theatreController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private TheatreDTO sampleTheatre;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(theatreController).build();
        objectMapper = new ObjectMapper();

        sampleTheatre = new TheatreDTO();
        sampleTheatre.setId(1L);
        sampleTheatre.setName("Sample Theatre");
        sampleTheatre.setCityName("Mumbai");
        sampleTheatre.setAddress("123 Main Street");
    }

    @Test
    void createTheatre_returnsCreated() throws Exception {
        Mockito.when(theatreService.saveTheatre(any(TheatreDTO.class))).thenReturn(sampleTheatre);

        mockMvc.perform(post("/api/v1/theatres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleTheatre)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(sampleTheatre.getId()))
                .andExpect(jsonPath("$.name").value(sampleTheatre.getName()))
                .andExpect(jsonPath("$.cityName").value(sampleTheatre.getCityName()));
    }

    @Test
    void patchTheatre_returnsUpdated() throws Exception {
        Map<String, Object> updates = Map.of("name", "Updated Theatre");

        Mockito.when(theatreService.getTheatreById(1L)).thenReturn(Optional.of(sampleTheatre));
        Mockito.when(theatreService.patchTheatre(any(TheatreDTO.class), eq(updates)))
                .thenReturn(sampleTheatre);

        mockMvc.perform(patch("/api/v1/theatres/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleTheatre.getId()))
                .andExpect(jsonPath("$.name").value(sampleTheatre.getName()));
    }

    @Test
    void getAllTheatres_returnsPaginatedResponse() throws Exception {
        PageResponse<TheatreDTO> response = new PageResponse<>(
                List.of(sampleTheatre),
                0, 10,
                1L,
                1,
                true
        );

        Mockito.when(theatreService.getAllTheatres(0, 10)).thenReturn(response);

        mockMvc.perform(get("/api/v1/theatres")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(sampleTheatre.getId()))
                .andExpect(jsonPath("$.content[0].name").value("Sample Theatre"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.pageNumber").value(0))
                .andExpect(jsonPath("$.last").value(true));
    }

    @Test
    void getAllTheatres_returnsEmptyPage() throws Exception {
        PageResponse<TheatreDTO> response = new PageResponse<>(
                List.of(), // empty content
                0, 10,
                0L, 0,
                true
        );

        Mockito.when(theatreService.getAllTheatres(0, 10)).thenReturn(response);

        mockMvc.perform(get("/api/v1/theatres")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0))
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.totalPages").value(0))
                .andExpect(jsonPath("$.pageNumber").value(0))
                .andExpect(jsonPath("$.last").value(true));
    }

    @Test
    void getTheatreById_returnsTheatre() throws Exception {
        Mockito.when(theatreService.getTheatreById(1L)).thenReturn(Optional.of(sampleTheatre));

        mockMvc.perform(get("/api/v1/theatres/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleTheatre.getId()))
                .andExpect(jsonPath("$.name").value(sampleTheatre.getName()));
    }

    @Test
    void getTheatreById_returnsNotFound() throws Exception {
        Mockito.when(theatreService.getTheatreById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/theatres/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getTheatresByCity_returnsList() throws Exception {
        Mockito.when(theatreService.getTheatresByCity("Mumbai")).thenReturn(List.of(sampleTheatre));

        mockMvc.perform(get("/api/v1/theatres/city/Mumbai"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].cityName").value("Mumbai"));
    }

    @Test
    void getTheatresByCity_returnsNoContentWhenEmpty() throws Exception {
        Mockito.when(theatreService.getTheatresByCity("Unknown")).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/theatres/city/Unknown"))
                .andExpect(status().isNoContent());
    }
}
