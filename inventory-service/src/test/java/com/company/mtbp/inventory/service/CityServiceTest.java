package com.company.mtbp.inventory.service;

import com.company.mtbp.inventory.dto.CityDTO;
import com.company.mtbp.inventory.entity.City;
import com.company.mtbp.inventory.exception.BadRequestException;
import com.company.mtbp.inventory.mapper.CityMapper;
import com.company.mtbp.inventory.repository.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CityMapper cityMapper;

    @InjectMocks
    private CityService cityService;

    private City sampleCity;
    private CityDTO sampleCityDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleCity = City.builder()
                .id(1L)
                .name("Mumbai")
                .build();

        sampleCityDTO = CityDTO.builder()
                .id(1L)
                .name("Mumbai")
                .build();
    }

    @Test
    void saveCity_success() {
        when(cityMapper.toEntity(sampleCityDTO)).thenReturn(sampleCity);
        when(cityRepository.save(sampleCity)).thenReturn(sampleCity);
        when(cityMapper.toDTO(sampleCity)).thenReturn(sampleCityDTO);

        CityDTO result = cityService.saveCity(sampleCityDTO);

        assertNotNull(result);
        assertEquals("Mumbai", result.getName());
        verify(cityRepository).save(sampleCity);
        verify(cityMapper).toDTO(sampleCity);
    }

    @Test
    void patchCity_updatesFieldSuccessfully() {
        when(cityMapper.toEntity(any())).thenReturn(sampleCity);
        when(cityRepository.save(any())).thenReturn(sampleCity);
        when(cityMapper.toDTO(any())).thenReturn(sampleCityDTO);

        sampleCityDTO.setName("OldName");
        CityDTO updated = cityService.patchCity(sampleCityDTO, Map.of("name", "NewName"));

        assertNotNull(updated);
        verify(cityRepository).save(any());
        assertEquals("NewName", updated.getName());
    }

    @Test
    void patchCity_invalidField_throwsBadRequest() {
        assertThrows(BadRequestException.class, () -> {
            cityService.patchCity(sampleCityDTO, Map.of("invalidField", "value"));
        });
    }

    @Test
    void getAllCities_returnsList() {
        when(cityRepository.findAll()).thenReturn(List.of(sampleCity));
        when(cityMapper.toDTOList(List.of(sampleCity))).thenReturn(List.of(sampleCityDTO));

        List<CityDTO> result = cityService.getAllCities();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Mumbai", result.get(0).getName());
    }

    @Test
    void getCityById_found() {
        when(cityRepository.findById(1L)).thenReturn(Optional.of(sampleCity));
        when(cityMapper.toDTO(sampleCity)).thenReturn(sampleCityDTO);

        Optional<CityDTO> result = cityService.getCityById(1L);

        assertTrue(result.isPresent());
        assertEquals("Mumbai", result.get().getName());
    }

    @Test
    void getCityById_notFound() {
        when(cityRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<CityDTO> result = cityService.getCityById(2L);

        assertTrue(result.isEmpty());
    }

    @Test
    void deleteCity_callsRepository() {
        cityService.deleteCity(1L);
        verify(cityRepository).deleteById(1L);
    }

    @Test
    void getCityByName_returnsOptional() {
        when(cityRepository.findByName("Mumbai")).thenReturn(Optional.of(sampleCity));

        Optional<City> result = cityService.getCityByName("Mumbai");

        assertTrue(result.isPresent());
        assertEquals("Mumbai", result.get().getName());
    }

    @Test
    void getCityByName_notFound() {
        when(cityRepository.findByName("Delhi")).thenReturn(Optional.empty());

        Optional<City> result = cityService.getCityByName("Delhi");

        assertTrue(result.isEmpty());
    }
}
