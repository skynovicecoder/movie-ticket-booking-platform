package com.company.mtbp.inventory.mapper;

import com.company.mtbp.inventory.dto.CityDTO;
import com.company.mtbp.inventory.entity.City;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CityMapper {
    CityDTO toDTO(City city);

    City toEntity(CityDTO dto);

    List<CityDTO> toDTOList(List<City> cities);
}
