package com.company.mtbp.inventory.service;

import com.company.mtbp.inventory.entity.City;
import com.company.mtbp.inventory.entity.Theatre;
import com.company.mtbp.inventory.repository.CityRepository;
import com.company.mtbp.inventory.repository.TheatreRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TheatreService {

    private final TheatreRepository theatreRepository;
    private final CityRepository cityRepository;

    public TheatreService(TheatreRepository theatreRepository, CityRepository cityRepository) {
        this.theatreRepository = theatreRepository;
        this.cityRepository = cityRepository;
    }

    public List<Theatre> getTheatresByCity(String cityName) {
        City city = cityRepository.findByName(cityName);
        if (city == null) return List.of();
        return theatreRepository.findByCity(city);
    }
}
