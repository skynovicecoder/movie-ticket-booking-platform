package com.company.mtbp.inventory.service;

import com.company.mtbp.inventory.dto.CityDTO;
import com.company.mtbp.inventory.entity.City;
import com.company.mtbp.inventory.exception.BadRequestException;
import com.company.mtbp.inventory.mapper.CityMapper;
import com.company.mtbp.inventory.repository.CityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class CityService {

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;


    public CityService(CityRepository cityRepository, CityMapper cityMapper) {
        this.cityRepository = cityRepository;
        this.cityMapper = cityMapper;
    }

    public CityDTO saveCity(CityDTO cityDTO) {
        City city = cityMapper.toEntity(cityDTO);
        City savedCity = cityRepository.save(city);
        return cityMapper.toDTO(savedCity);
    }

    public CityDTO patchCity(CityDTO cityDTO, Map<String, Object> updates) {
        updates.forEach((key, value) -> {
            try {
                Field field = CityDTO.class.getDeclaredField(key);
                field.setAccessible(true);
                field.set(cityDTO, value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                log.warn("Invalid City field: {}", key);
                throw new BadRequestException("Invalid City field: " + key);
            }
        });
        return saveCity(cityDTO);
    }

    public List<CityDTO> getAllCities() {
        List<City> cities = cityRepository.findAll();
        return cityMapper.toDTOList(cities);
    }

    public Optional<CityDTO> getCityById(Long id) {
        return cityRepository.findById(id)
                .map(cityMapper::toDTO);
    }

    public void deleteCity(Long id) {
        cityRepository.deleteById(id);
    }

    public Optional<City> getCityByName(String cityName) {
        return cityRepository.findByName(cityName);
    }
}
