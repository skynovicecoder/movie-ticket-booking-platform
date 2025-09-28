package com.company.mtbp.inventory.mapper;

import com.company.mtbp.inventory.dto.SeatDTO;
import com.company.mtbp.inventory.entity.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SeatMapper {

    @Mapping(source = "theatre.id", target = "theatreId")
    @Mapping(source = "theatre.name", target = "theatreName") // optional, if theatre has name field
    @Mapping(source = "show.id", target = "showId")
    SeatDTO toDTO(Seat seat);

    @Mapping(source = "theatreId", target = "theatre.id")
    @Mapping(source = "showId", target = "show.id")
    @Mapping(target = "theatre", ignore = true) // handled manually or via service
    @Mapping(target = "show", ignore = true)
        // handled manually or via service
    Seat toEntity(SeatDTO dto);

    List<SeatDTO> toDTOList(List<Seat> seats);
}
