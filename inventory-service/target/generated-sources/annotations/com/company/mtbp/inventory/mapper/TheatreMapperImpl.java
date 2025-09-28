package com.company.mtbp.inventory.mapper;

import com.company.mtbp.inventory.dto.TheatreDTO;
import com.company.mtbp.inventory.entity.City;
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
public class TheatreMapperImpl implements TheatreMapper {

    @Override
    public TheatreDTO toDTO(Theatre theatre) {
        if ( theatre == null ) {
            return null;
        }

        TheatreDTO theatreDTO = new TheatreDTO();

        theatreDTO.setCityId( theatreCityId( theatre ) );
        theatreDTO.setCityName( theatreCityName( theatre ) );
        theatreDTO.setId( theatre.getId() );
        theatreDTO.setName( theatre.getName() );
        theatreDTO.setAddress( theatre.getAddress() );
        theatreDTO.setTotalSeats( theatre.getTotalSeats() );

        return theatreDTO;
    }

    @Override
    public Theatre toEntity(TheatreDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Theatre theatre = new Theatre();

        theatre.setId( dto.getId() );
        theatre.setName( dto.getName() );
        theatre.setAddress( dto.getAddress() );
        theatre.setTotalSeats( dto.getTotalSeats() );

        return theatre;
    }

    @Override
    public List<TheatreDTO> toDTOList(List<Theatre> theatres) {
        if ( theatres == null ) {
            return null;
        }

        List<TheatreDTO> list = new ArrayList<TheatreDTO>( theatres.size() );
        for ( Theatre theatre : theatres ) {
            list.add( toDTO( theatre ) );
        }

        return list;
    }

    private Long theatreCityId(Theatre theatre) {
        City city = theatre.getCity();
        if ( city == null ) {
            return null;
        }
        return city.getId();
    }

    private String theatreCityName(Theatre theatre) {
        City city = theatre.getCity();
        if ( city == null ) {
            return null;
        }
        return city.getName();
    }
}
