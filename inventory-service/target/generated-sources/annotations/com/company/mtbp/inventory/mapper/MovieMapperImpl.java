package com.company.mtbp.inventory.mapper;

import com.company.mtbp.inventory.dto.MovieDTO;
import com.company.mtbp.inventory.entity.Movie;
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
public class MovieMapperImpl implements MovieMapper {

    @Override
    public MovieDTO toDTO(Movie movie) {
        if ( movie == null ) {
            return null;
        }

        MovieDTO movieDTO = new MovieDTO();

        movieDTO.setId( movie.getId() );
        movieDTO.setTitle( movie.getTitle() );
        movieDTO.setGenre( movie.getGenre() );
        movieDTO.setDurationMinutes( movie.getDurationMinutes() );
        movieDTO.setLanguage( movie.getLanguage() );
        movieDTO.setReleaseDate( movie.getReleaseDate() );
        movieDTO.setRating( movie.getRating() );

        return movieDTO;
    }

    @Override
    public Movie toEntity(MovieDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Movie movie = new Movie();

        movie.setId( dto.getId() );
        movie.setTitle( dto.getTitle() );
        movie.setGenre( dto.getGenre() );
        movie.setDurationMinutes( dto.getDurationMinutes() );
        movie.setLanguage( dto.getLanguage() );
        movie.setReleaseDate( dto.getReleaseDate() );
        movie.setRating( dto.getRating() );

        return movie;
    }

    @Override
    public List<MovieDTO> toDTOList(List<Movie> movies) {
        if ( movies == null ) {
            return null;
        }

        List<MovieDTO> list = new ArrayList<MovieDTO>( movies.size() );
        for ( Movie movie : movies ) {
            list.add( toDTO( movie ) );
        }

        return list;
    }
}
