package com.company.mtbp.inventory.mapper;

import com.company.mtbp.inventory.dto.SeatDTO;
import com.company.mtbp.inventory.entity.Seat;
import com.company.mtbp.inventory.entity.Show;
import com.company.mtbp.inventory.entity.Theatre;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-28T20:32:13+0530",
    comments = "version: 1.6.0.Beta2, compiler: javac, environment: Java 21 (Eclipse Adoptium)"
)
@Component
public class SeatMapperImpl implements SeatMapper {

    @Override
    public SeatDTO toDTO(Seat seat) {
        if ( seat == null ) {
            return null;
        }

        SeatDTO.SeatDTOBuilder seatDTO = SeatDTO.builder();

        seatDTO.theatreId( seatTheatreId( seat ) );
        seatDTO.theatreName( seatTheatreName( seat ) );
        seatDTO.showId( seatShowId( seat ) );
        seatDTO.id( seat.getId() );
        seatDTO.seatNumber( seat.getSeatNumber() );
        seatDTO.seatType( seat.getSeatType() );
        seatDTO.available( seat.getAvailable() );

        return seatDTO.build();
    }

    @Override
    public Seat toEntity(SeatDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Seat seat = new Seat();

        seat.setTheatre( seatDTOToTheatre( dto ) );
        seat.setShow( seatDTOToShow( dto ) );
        seat.setId( dto.getId() );
        seat.setSeatNumber( dto.getSeatNumber() );
        seat.setSeatType( dto.getSeatType() );
        seat.setAvailable( dto.getAvailable() );

        return seat;
    }

    @Override
    public List<SeatDTO> toDTOList(List<Seat> seats) {
        if ( seats == null ) {
            return null;
        }

        List<SeatDTO> list = new ArrayList<SeatDTO>( seats.size() );
        for ( Seat seat : seats ) {
            list.add( toDTO( seat ) );
        }

        return list;
    }

    private Long seatTheatreId(Seat seat) {
        Theatre theatre = seat.getTheatre();
        if ( theatre == null ) {
            return null;
        }
        return theatre.getId();
    }

    private String seatTheatreName(Seat seat) {
        Theatre theatre = seat.getTheatre();
        if ( theatre == null ) {
            return null;
        }
        return theatre.getName();
    }

    private Long seatShowId(Seat seat) {
        Show show = seat.getShow();
        if ( show == null ) {
            return null;
        }
        return show.getId();
    }

    protected Theatre seatDTOToTheatre(SeatDTO seatDTO) {
        if ( seatDTO == null ) {
            return null;
        }

        Theatre theatre = new Theatre();

        theatre.setId( seatDTO.getTheatreId() );

        return theatre;
    }

    protected Show seatDTOToShow(SeatDTO seatDTO) {
        if ( seatDTO == null ) {
            return null;
        }

        Show show = new Show();

        show.setId( seatDTO.getShowId() );

        return show;
    }
}
