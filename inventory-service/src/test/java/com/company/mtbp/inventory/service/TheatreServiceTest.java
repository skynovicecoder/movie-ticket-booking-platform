package com.company.mtbp.inventory.service;

import com.company.mtbp.inventory.dto.TheatreDTO;
import com.company.mtbp.inventory.entity.City;
import com.company.mtbp.inventory.entity.Seat;
import com.company.mtbp.inventory.entity.Theatre;
import com.company.mtbp.inventory.exception.BadRequestException;
import com.company.mtbp.inventory.exception.ResourceNotFoundException;
import com.company.mtbp.inventory.mapper.TheatreMapper;
import com.company.mtbp.inventory.repository.CityRepository;
import com.company.mtbp.inventory.repository.SeatRepository;
import com.company.mtbp.inventory.repository.TheatreRepository;
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

class TheatreServiceTest {

    @Mock
    private TheatreRepository theatreRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private TheatreMapper theatreMapper;

    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private TheatreService theatreService;

    private TheatreDTO sampleDTO;
    private Theatre sampleTheatre;
    private City sampleCity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleCity = new City();
        sampleCity.setId(1L);
        sampleCity.setName("Mumbai");

        sampleTheatre = new Theatre();
        sampleTheatre.setId(1L);
        sampleTheatre.setName("PVR");
        sampleTheatre.setCity(sampleCity);

        sampleDTO = new TheatreDTO();
        sampleDTO.setId(1L);
        sampleDTO.setName("PVR");
        sampleDTO.setCityId(1L);
        sampleDTO.setCityName("Mumbai");
        sampleDTO.setTotalSeats(10);
    }

    @Test
    void saveTheatre_byCityId_success() {
        when(cityRepository.findById(1L)).thenReturn(Optional.of(sampleCity));
        when(theatreMapper.toEntity(sampleDTO)).thenReturn(sampleTheatre);
        when(theatreRepository.save(sampleTheatre)).thenReturn(sampleTheatre);
        when(theatreMapper.toDTO(sampleTheatre)).thenReturn(sampleDTO);

        TheatreDTO result = theatreService.saveTheatre(sampleDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(seatRepository).saveAll(anyList());
    }

    @Test
    void saveTheatre_byCityName_success() {
        sampleDTO.setCityId(null);
        when(cityRepository.findByName("Mumbai")).thenReturn(Optional.of(sampleCity));
        when(theatreMapper.toEntity(sampleDTO)).thenReturn(sampleTheatre);
        when(theatreRepository.save(sampleTheatre)).thenReturn(sampleTheatre);
        when(theatreMapper.toDTO(sampleTheatre)).thenReturn(sampleDTO);

        TheatreDTO result = theatreService.saveTheatre(sampleDTO);

        assertNotNull(result);
        assertEquals("PVR", result.getName());
        verify(seatRepository).saveAll(anyList());
    }

    @Test
    void saveTheatre_cityNotFound_throws() {
        when(cityRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> theatreService.saveTheatre(sampleDTO));

        sampleDTO.setCityId(null);
        when(cityRepository.findByName("Mumbai")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> theatreService.saveTheatre(sampleDTO));
    }

    @Test
    void prepareSeatsForTheatre_countsCorrect() {
        List<Seat> seats = theatreService.prepareSeatsForTheatre(sampleTheatre, 10);

        assertEquals(10, seats.size());
        assertEquals("VIP", seats.get(0).getSeatType());
        assertEquals("PREMIUM", seats.get(1 + 1).getSeatType());
        assertEquals("REGULAR", seats.get(5).getSeatType());
    }

    @Test
    void patchTheatre_success() {
        Map<String, Object> updates = Map.of("name", "INOX", "totalSeats", 20);

        when(cityRepository.findById(1L)).thenReturn(Optional.of(sampleCity));
        when(theatreMapper.toEntity(any())).thenReturn(sampleTheatre);
        when(theatreRepository.save(sampleTheatre)).thenReturn(sampleTheatre);
        when(theatreMapper.toDTO(sampleTheatre)).thenReturn(sampleDTO);

        TheatreDTO updated = theatreService.patchTheatre(sampleDTO, updates);

        assertNotNull(updated);
        verify(seatRepository).saveAll(anyList());
    }

    @Test
    void patchTheatre_invalidField_throws() {
        Map<String, Object> updates = Map.of("invalidField", "value");
        assertThrows(BadRequestException.class, () -> theatreService.patchTheatre(sampleDTO, updates));
    }

    @Test
    void getAllTheatres_success() {
        when(theatreRepository.findAll()).thenReturn(List.of(sampleTheatre));
        when(theatreMapper.toDTOList(List.of(sampleTheatre))).thenReturn(List.of(sampleDTO));

        List<TheatreDTO> list = theatreService.getAllTheatres();

        assertEquals(1, list.size());
        assertEquals("PVR", list.getFirst().getName());
    }

    @Test
    void getTheatreById_found() {
        when(theatreRepository.findById(1L)).thenReturn(Optional.of(sampleTheatre));
        when(theatreMapper.toDTO(sampleTheatre)).thenReturn(sampleDTO);

        Optional<TheatreDTO> dto = theatreService.getTheatreById(1L);

        assertTrue(dto.isPresent());
        assertEquals("PVR", dto.get().getName());
    }

    @Test
    void getTheatreById_notFound() {
        when(theatreRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<TheatreDTO> dto = theatreService.getTheatreById(1L);
        assertTrue(dto.isEmpty());
    }

    @Test
    void getTheatresByCity_found() {
        when(cityRepository.findByName("Mumbai")).thenReturn(Optional.of(sampleCity));
        when(theatreRepository.findByCity(sampleCity)).thenReturn(List.of(sampleTheatre));
        when(theatreMapper.toDTOList(List.of(sampleTheatre))).thenReturn(List.of(sampleDTO));

        List<TheatreDTO> result = theatreService.getTheatresByCity("Mumbai");

        assertEquals(1, result.size());
        assertEquals("PVR", result.getFirst().getName());
    }

    @Test
    void getTheatresByCity_cityNotFound_returnsEmpty() {
        when(cityRepository.findByName("Mumbai")).thenReturn(Optional.empty());
        List<TheatreDTO> result = theatreService.getTheatresByCity("Mumbai");
        assertTrue(result.isEmpty());
    }
}
