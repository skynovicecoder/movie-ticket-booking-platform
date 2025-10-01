package com.company.mtbp.inventory.service;

import com.company.mtbp.inventory.dto.SeatDTO;
import com.company.mtbp.inventory.entity.Seat;
import com.company.mtbp.inventory.entity.Show;
import com.company.mtbp.inventory.entity.Theatre;
import com.company.mtbp.inventory.exception.ResourceNotFoundException;
import com.company.mtbp.inventory.mapper.SeatMapper;
import com.company.mtbp.inventory.pagedto.PageResponse;
import com.company.mtbp.inventory.repository.SeatRepository;
import com.company.mtbp.inventory.repository.ShowRepository;
import com.company.mtbp.inventory.repository.TheatreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SeatServiceTest {

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private TheatreRepository theatreRepository;

    @Mock
    private ShowRepository showRepository;

    @Mock
    private SeatMapper seatMapper;

    @InjectMocks
    private SeatService seatService;

    private Seat sampleSeat;
    private SeatDTO sampleSeatDTO;
    private Theatre sampleTheatre;
    private Show sampleShow;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleTheatre = new Theatre();
        sampleTheatre.setId(1L);

        sampleShow = new Show();
        sampleShow.setId(1L);

        sampleSeat = new Seat();
        sampleSeat.setId(1L);
        sampleSeat.setSeatNumber("A1");
        sampleSeat.setTheatre(sampleTheatre);
        sampleSeat.setShow(sampleShow);

        sampleSeatDTO = new SeatDTO();
        sampleSeatDTO.setId(1L);
        sampleSeatDTO.setSeatNumber("A1");
        sampleSeatDTO.setTheatreId(1L);
        sampleSeatDTO.setShowId(1L);
    }

    @Test
    void addSeat_success() {
        when(theatreRepository.findById(1L)).thenReturn(Optional.of(sampleTheatre));
        when(showRepository.findById(1L)).thenReturn(Optional.of(sampleShow));
        when(seatMapper.toEntity(sampleSeatDTO)).thenReturn(sampleSeat);
        when(seatRepository.save(sampleSeat)).thenReturn(sampleSeat);
        when(seatMapper.toDTO(sampleSeat)).thenReturn(sampleSeatDTO);

        SeatDTO result = seatService.addSeat(sampleSeatDTO);

        assertNotNull(result);
        assertEquals("A1", result.getSeatNumber());
        verify(seatRepository).save(sampleSeat);
    }

    @Test
    void addSeat_theatreNotFound_throws() {
        when(theatreRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> seatService.addSeat(sampleSeatDTO));
    }

    @Test
    void addSeat_showNotFound_throws() {
        when(theatreRepository.findById(1L)).thenReturn(Optional.of(sampleTheatre));
        when(showRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NullPointerException.class, () -> seatService.addSeat(sampleSeatDTO));
    }

    @Test
    void deleteSeat_byId_success() {
        when(seatRepository.findById(1L)).thenReturn(Optional.of(sampleSeat));
        assertDoesNotThrow(() -> seatService.deleteSeat(1L, null, null));
        verify(seatRepository).delete(sampleSeat);
    }

    @Test
    void deleteSeat_byId_notFound_throws() {
        when(seatRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> seatService.deleteSeat(1L, null, null));
    }

    @Test
    void deleteSeat_byTheatreAndSeatNumber_success() {
        when(seatRepository.findByTheatreIdAndSeatNumber(1L, "A1")).thenReturn(Optional.of(sampleSeat));
        assertDoesNotThrow(() -> seatService.deleteSeat(null, 1L, "A1"));
        verify(seatRepository).delete(sampleSeat);
    }

    @Test
    void deleteSeat_byTheatreAndSeatNumber_notFound_throws() {
        when(seatRepository.findByTheatreIdAndSeatNumber(1L, "A1")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> seatService.deleteSeat(null, 1L, "A1"));
    }

    @Test
    void patchSeat_success() throws Exception {
        when(seatRepository.findById(1L)).thenReturn(Optional.of(sampleSeat));
        when(seatRepository.save(sampleSeat)).thenReturn(sampleSeat);
        when(seatMapper.toDTO(sampleSeat)).thenReturn(sampleSeatDTO);

        SeatDTO updated = seatService.patchSeat(1L, 1L, Map.of("seatNumber", "A2"));

        assertNotNull(updated);
        verify(seatRepository).save(sampleSeat);
    }

    @Test
    void patchSeat_invalidTheatre_throws() {
        Theatre otherTheatre = new Theatre();
        otherTheatre.setId(2L);
        sampleSeat.setTheatre(otherTheatre);

        when(seatRepository.findById(1L)).thenReturn(Optional.of(sampleSeat));

        assertThrows(ResourceNotFoundException.class, () ->
                seatService.patchSeat(1L, 1L, Map.of("seatNumber", "A2"))
        );
    }

    @Test
    void patchSeat_invalidField_throws() {
        when(seatRepository.findById(1L)).thenReturn(Optional.of(sampleSeat));
        assertThrows(ResourceNotFoundException.class, () ->
                seatService.patchSeat(1L, 1L, Map.of("invalidField", "value"))
        );
    }

    @Test
    void getSeats_paginationWorksCorrectly() {
        // Sample seats
        SeatDTO seat1 = new SeatDTO(1L, "R1", "REGULAR", true, 101L, "Big Theatre", 201L);
        SeatDTO seat2 = new SeatDTO(2L, "R2", "REGULAR", true, 101L, "Big Theatre", 201L);
        SeatDTO seat3 = new SeatDTO(3L, "R3", "REGULAR", true, 101L, "Big Theatre", 201L);
        List<SeatDTO> allSeats = List.of(seat1, seat2, seat3);

        SeatService seatServiceSpy = Mockito.spy(seatService);
        doReturn(allSeats).when(seatServiceSpy).getSeatsByTheatre(10L);

        PageResponse<SeatDTO> result = seatServiceSpy.getSeats(
                null,
                10L,
                null,
                0,
                2
        );

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(3, result.getTotalElements());
        assertEquals(2, result.getTotalPages());
        assertEquals(0, result.getPageNumber());
        assertFalse(result.isLast());

        assertEquals("R1", result.getContent().get(0).getSeatNumber());
        assertEquals("R2", result.getContent().get(1).getSeatNumber());

        PageResponse<SeatDTO> lastPage = seatServiceSpy.getSeats(null, 10L, null, 1, 2);
        assertEquals(1, lastPage.getContent().size());
        assertEquals("R3", lastPage.getContent().getFirst().getSeatNumber());
        assertTrue(lastPage.isLast());
    }

    @Test
    void getSeatById_success() {
        when(seatRepository.findById(1L)).thenReturn(Optional.of(sampleSeat));
        when(seatMapper.toDTO(sampleSeat)).thenReturn(sampleSeatDTO);

        SeatDTO result = seatService.getSeatById(1L);
        assertEquals("A1", result.getSeatNumber());
    }

    @Test
    void getSeatById_notFound_throws() {
        when(seatRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> seatService.getSeatById(1L));
    }

    @Test
    void updateShowForTheatre_success() {
        when(showRepository.findById(1L)).thenReturn(Optional.of(sampleShow));
        when(seatRepository.updateShowIdByTheatreId(1L, sampleShow.getId())).thenReturn(5);

        int updated = seatService.updateShowForTheatre(1L, 1L);

        assertEquals(5, updated);
    }

    @Test
    void updateShowForTheatre_showNotFound_throws() {
        when(showRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> seatService.updateShowForTheatre(1L, 1L));
    }
}
