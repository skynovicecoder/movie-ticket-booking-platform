package com.company.mtbp.inventory.mapper;

import com.company.mtbp.inventory.dto.TheatreDTO;
import com.company.mtbp.inventory.entity.Theatre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TheatreMapper {
    @Mapping(source = "city.id", target = "cityId")
    @Mapping(source = "city.name", target = "cityName")
    TheatreDTO toDTO(Theatre theatre);

    Theatre toEntity(TheatreDTO dto);

    List<TheatreDTO> toDTOList(List<Theatre> theatres);
}
