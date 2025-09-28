package com.company.mtbp.inventory.mapper;

import com.company.mtbp.inventory.dto.CityDTO;
import com.company.mtbp.inventory.entity.City;
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
public class CityMapperImpl implements CityMapper {

    @Override
    public CityDTO toDTO(City city) {
        if ( city == null ) {
            return null;
        }

        CityDTO cityDTO = new CityDTO();

        cityDTO.setId( city.getId() );
        cityDTO.setName( city.getName() );

        return cityDTO;
    }

    @Override
    public City toEntity(CityDTO dto) {
        if ( dto == null ) {
            return null;
        }

        City city = new City();

        city.setId( dto.getId() );
        city.setName( dto.getName() );

        return city;
    }

    @Override
    public List<CityDTO> toDTOList(List<City> cities) {
        if ( cities == null ) {
            return null;
        }

        List<CityDTO> list = new ArrayList<CityDTO>( cities.size() );
        for ( City city : cities ) {
            list.add( toDTO( city ) );
        }

        return list;
    }
}
