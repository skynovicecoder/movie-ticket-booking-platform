package com.company.mtbp.inventory.mapper;

import com.company.mtbp.inventory.dto.MovieDTO;
import com.company.mtbp.inventory.entity.Movie;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MovieMapper {
    MovieDTO toDTO(Movie movie);

    Movie toEntity(MovieDTO dto);

    List<MovieDTO> toDTOList(List<Movie> movies);
}
