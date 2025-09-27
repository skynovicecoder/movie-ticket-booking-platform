package com.company.mtbp.inventory.service;

import com.company.mtbp.inventory.entity.City;
import com.company.mtbp.inventory.entity.Theatre;
import com.company.mtbp.inventory.repository.CityRepository;
import com.company.mtbp.inventory.repository.TheatreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TheatreService {

    private final TheatreRepository theatreRepository;
    private final CityRepository cityRepository;

    public TheatreService(TheatreRepository theatreRepository, CityRepository cityRepository) {
        this.theatreRepository = theatreRepository;
        this.cityRepository = cityRepository;
    }

    public Theatre saveTheatre(Theatre theatre) {
        return theatreRepository.save(theatre);
    }

    public List<Theatre> getAllTheatres() {
        return theatreRepository.findAll();
    }

    public Optional<Theatre> getTheatreById(Long id) {
        return theatreRepository.findById(id);
    }

    public List<Theatre> getTheatresByCity(String cityName) {
        Optional<City> city = cityRepository.findByName(cityName);
        if (city.isEmpty()) return List.of();
        return theatreRepository.findByCity(city.get());
    }
}
