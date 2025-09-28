package com.company.mtbp.inventory.mapper;

import com.company.mtbp.inventory.dto.ShowDTO;
import com.company.mtbp.inventory.entity.Show;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ShowMapper {

    @Mapping(source = "movie.id", target = "movieId")
    @Mapping(source = "movie.title", target = "movieTitle")
    @Mapping(source = "theatre.id", target = "theatreId")
    @Mapping(source = "theatre.name", target = "theatreName")
    ShowDTO toDTO(Show show);

    @Mapping(source = "movieId", target = "movie.id")
    @Mapping(source = "theatreId", target = "theatre.id")
    Show toEntity(ShowDTO dto);

    List<ShowDTO> toDTOList(List<Show> shows);
}
