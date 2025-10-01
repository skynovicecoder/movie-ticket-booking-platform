package com.company.mtbp.inventory.controller;

import com.company.mtbp.inventory.dto.CityDTO;
import com.company.mtbp.inventory.pagedto.PageResponse;
import com.company.mtbp.inventory.service.CityService;
import com.github.javafaker.Faker;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CityControllerTest {

    @Mock
    private CityService cityService;

    @InjectMocks
    private CityController cityController;

    private MockMvc mockMvc;
    private CityDTO sampleCity;

    @BeforeEach
    void setup() {
        Faker faker = new Faker();
        mockMvc = MockMvcBuilders.standaloneSetup(cityController).build();

        sampleCity = new CityDTO();
        sampleCity.setId(1L);
        sampleCity.setName(faker.address().cityName());
    }

    @Test
    void createCity_returnsCreatedCity() throws Exception {
        Mockito.when(cityService.saveCity(any(CityDTO.class))).thenReturn(sampleCity);

        mockMvc.perform(post("/api/v1/cities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"" + sampleCity.getName() + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(sampleCity.getId()))
                .andExpect(jsonPath("$.name").value(sampleCity.getName()));
    }

    @Test
    void getAllCities_returnsPaginatedResponse() throws Exception {
        PageResponse<CityDTO> response = new PageResponse<>(
                List.of(sampleCity),
                0, 10,
                1L,
                1,
                true
        );

        Mockito.when(cityService.getAllCities(0, 10)).thenReturn(response);

        mockMvc.perform(get("/api/v1/cities")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.pageNumber").value(0))
                .andExpect(jsonPath("$.last").value(true));
    }

    @Test
    void getCityById_returnsCity() throws Exception {
        Mockito.when(cityService.getCityById(1L)).thenReturn(Optional.of(sampleCity));

        mockMvc.perform(get("/api/v1/cities/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleCity.getId()))
                .andExpect(jsonPath("$.name").value(sampleCity.getName()));
    }

    @Test
    void getCityById_returnsNotFound_whenCityMissing() throws Exception {
        Mockito.when(cityService.getCityById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/cities/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void patchCity_updatesCity() throws Exception {
        CityDTO updatedCity = new CityDTO();
        updatedCity.setId(sampleCity.getId());
        updatedCity.setName("UpdatedName");

        Mockito.when(cityService.getCityById(1L)).thenReturn(Optional.of(sampleCity));
        Mockito.when(cityService.patchCity(any(CityDTO.class), any(Map.class))).thenReturn(updatedCity);

        mockMvc.perform(patch("/api/v1/cities/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"UpdatedName\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UpdatedName"));
    }

    @Test
    void patchCity_returnsNotFound_whenCityMissing() throws Exception {
        Mockito.when(cityService.getCityById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(patch("/api/v1/cities/update/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"UpdatedName\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCity_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/cities/1"))
                .andExpect(status().isNoContent());
        Mockito.verify(cityService).deleteCity(1L);
    }
}
