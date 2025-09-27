package com.company.mtbp.inventory.controller;

import com.company.mtbp.inventory.entity.City;
import com.company.mtbp.inventory.service.CityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/cities")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    // Create a new city
    @PostMapping
    public City createCity(@RequestBody City city) {
        return cityService.saveCity(city);
    }

    // Get all cities
    @GetMapping
    public List<City> getAllCities() {
        return cityService.getAllCities();
    }

    // Get city by ID
    @GetMapping("/{id}")
    public ResponseEntity<City> getCityById(@PathVariable("id") Long id) {
        Optional<City> city = cityService.getCityById(id);
        return city.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Patch city using reflection
    @PatchMapping("/update/{id}")
    public ResponseEntity<City> patchCity(@PathVariable("id") Long id,
                                          @RequestBody Map<String, Object> updates) {

        Optional<City> optionalCity = cityService.getCityById(id);
        if (optionalCity.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        City city = optionalCity.get();

        updates.forEach((key, value) -> {
            try {
                Field field = City.class.getDeclaredField(key);
                field.setAccessible(true);
                field.set(city, value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                System.out.println("Invalid field: " + key);
            }
        });

        City updatedCity = cityService.saveCity(city);
        return ResponseEntity.ok(updatedCity);
    }

    // Delete city
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable("id") Long id) {
        cityService.deleteCity(id);
        return ResponseEntity.noContent().build();
    }

}
