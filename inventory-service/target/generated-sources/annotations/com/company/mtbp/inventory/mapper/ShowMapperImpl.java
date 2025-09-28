package com.company.mtbp.inventory.mapper;

import com.company.mtbp.inventory.dto.ShowDTO;
import com.company.mtbp.inventory.entity.Movie;
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
public class ShowMapperImpl implements ShowMapper {

    @Override
    public ShowDTO toDTO(Show show) {
        if ( show == null ) {
            return null;
        }

        ShowDTO showDTO = new ShowDTO();

        showDTO.setMovieId( showMovieId( show ) );
        showDTO.setMovieTitle( showMovieTitle( show ) );
        showDTO.setTheatreId( showTheatreId( show ) );
        showDTO.setTheatreName( showTheatreName( show ) );
        showDTO.setId( show.getId() );
        showDTO.setShowDate( show.getShowDate() );
        showDTO.setStartTime( show.getStartTime() );
        showDTO.setEndTime( show.getEndTime() );
        showDTO.setPricePerTicket( show.getPricePerTicket() );
        showDTO.setShowType( show.getShowType() );

        return showDTO;
    }

    @Override
    public Show toEntity(ShowDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Show show = new Show();

        show.setMovie( showDTOToMovie( dto ) );
        show.setTheatre( showDTOToTheatre( dto ) );
        show.setId( dto.getId() );
        show.setShowDate( dto.getShowDate() );
        show.setStartTime( dto.getStartTime() );
        show.setEndTime( dto.getEndTime() );
        show.setPricePerTicket( dto.getPricePerTicket() );
        show.setShowType( dto.getShowType() );

        return show;
    }

    @Override
    public List<ShowDTO> toDTOList(List<Show> shows) {
        if ( shows == null ) {
            return null;
        }

        List<ShowDTO> list = new ArrayList<ShowDTO>( shows.size() );
        for ( Show show : shows ) {
            list.add( toDTO( show ) );
        }

        return list;
    }

    private Long showMovieId(Show show) {
        Movie movie = show.getMovie();
        if ( movie == null ) {
            return null;
        }
        return movie.getId();
    }

    private String showMovieTitle(Show show) {
        Movie movie = show.getMovie();
        if ( movie == null ) {
            return null;
        }
        return movie.getTitle();
    }

    private Long showTheatreId(Show show) {
        Theatre theatre = show.getTheatre();
        if ( theatre == null ) {
            return null;
        }
        return theatre.getId();
    }

    private String showTheatreName(Show show) {
        Theatre theatre = show.getTheatre();
        if ( theatre == null ) {
            return null;
        }
        return theatre.getName();
    }

    protected Movie showDTOToMovie(ShowDTO showDTO) {
        if ( showDTO == null ) {
            return null;
        }

        Movie movie = new Movie();

        movie.setId( showDTO.getMovieId() );

        return movie;
    }

    protected Theatre showDTOToTheatre(ShowDTO showDTO) {
        if ( showDTO == null ) {
            return null;
        }

        Theatre theatre = new Theatre();

        theatre.setId( showDTO.getTheatreId() );

        return theatre;
    }
}
