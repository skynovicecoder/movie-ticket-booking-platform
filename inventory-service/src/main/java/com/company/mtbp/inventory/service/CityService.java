package com.company.mtbp.inventory.service;

import com.company.mtbp.inventory.entity.City;
import com.company.mtbp.inventory.repository.CityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CityService {

    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public City saveCity(City city) {
        return cityRepository.save(city);
    }

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    public Optional<City> getCityById(Long id) {
        return cityRepository.findById(id);
    }

    public void deleteCity(Long id) {
        cityRepository.deleteById(id);
    }

    public Optional<City> getCityByName(String cityName) {
        return cityRepository.findByName(cityName);
    }
}
