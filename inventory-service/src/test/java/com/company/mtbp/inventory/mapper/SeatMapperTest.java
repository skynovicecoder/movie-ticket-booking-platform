package com.company.mtbp.inventory.mapper;

import com.company.mtbp.inventory.dto.SeatDTO;
import com.company.mtbp.inventory.entity.Seat;
import com.company.mtbp.inventory.entity.Show;
import com.company.mtbp.inventory.entity.Theatre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SeatMapperTest {

    private SeatMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(SeatMapper.class);
    }

    @Test
    void toDTO_shouldMapEntityToDTO() {
        Theatre theatre = new Theatre();
        theatre.setId(1L);
        theatre.setName("Big Cinema");

        Show show = new Show();
        show.setId(10L);

        Seat seat = new Seat();
        seat.setId(100L);
        seat.setSeatNumber("A1");
        seat.setSeatType("VIP");
        seat.setTheatre(theatre);
        seat.setShow(show);
        seat.setAvailable(true);

        SeatDTO dto = mapper.toDTO(seat);

        assertNotNull(dto);
        assertEquals(100L, dto.getId());
        assertEquals("A1", dto.getSeatNumber());
        assertEquals("VIP", dto.getSeatType());
        assertEquals(true, dto.getAvailable());
        assertEquals(1L, dto.getTheatreId());
        assertEquals("Big Cinema", dto.getTheatreName());
        assertEquals(10L, dto.getShowId());
    }

    @Test
    void toEntity_shouldMapDTOToEntity() {
        SeatDTO dto = new SeatDTO();
        dto.setId(200L);
        dto.setSeatNumber("B2");
        dto.setSeatType("PREMIUM");
        dto.setTheatreId(2L);
        dto.setShowId(20L);
        dto.setAvailable(false);

        Seat entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(200L, entity.getId());
        assertEquals("B2", entity.getSeatNumber());
        assertEquals("PREMIUM", entity.getSeatType());
        assertEquals(false, entity.getAvailable());
    }

    @Test
    void toDTOList_shouldMapListOfEntities() {
        Seat seat1 = new Seat();
        seat1.setId(1L);
        seat1.setSeatNumber("A1");

        Seat seat2 = new Seat();
        seat2.setId(2L);
        seat2.setSeatNumber("B1");

        List<SeatDTO> dtoList = mapper.toDTOList(List.of(seat1, seat2));

        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());
        assertEquals("A1", dtoList.get(0).getSeatNumber());
        assertEquals("B1", dtoList.get(1).getSeatNumber());
    }
}
