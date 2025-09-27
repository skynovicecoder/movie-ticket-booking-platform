package com.company.mtbp.inventory.controller;

import com.company.mtbp.inventory.entity.City;
import com.company.mtbp.inventory.entity.Theatre;
import com.company.mtbp.inventory.repository.CityRepository;
import com.company.mtbp.inventory.service.TheatreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/theatres")
public class TheatreController {

    private final TheatreService theatreService;
    private final CityRepository cityRepository;;

    public TheatreController(TheatreService theatreService, CityRepository cityRepository) {
        this.theatreService = theatreService;
        this.cityRepository = cityRepository;
    }

    // Create a new theatre
    @PostMapping
    public Theatre createTheatre(@RequestBody Theatre theatre) {
        City cityObj = theatre.getCity();
        if(cityObj.getId()==null) {
            String cityName = cityObj.getName();
            Optional<City> obj = cityRepository.findByName(cityName);
            cityObj.setId(obj.get().getId());
        }
        return theatreService.saveTheatre(theatre);
    }

    // Patch / update theatre fields dynamically
    @PatchMapping("/update/{id}")
    public ResponseEntity<Theatre> patchTheatre(@PathVariable("id") Long id,
                                                @RequestBody Map<String, Object> updates) {
        Optional<Theatre> optionalTheatre = theatreService.getTheatreById(id);
        if (optionalTheatre.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Theatre theatre = optionalTheatre.get();

        updates.forEach((key, value) -> {
            try {
                Field field = Theatre.class.getDeclaredField(key);
                field.setAccessible(true);

                // Handle special types if needed
                if (field.getType().equals(Integer.class) && value instanceof Number) {
                    field.set(theatre, ((Number) value).intValue());
                } else {
                    field.set(theatre, value);
                }

            } catch (NoSuchFieldException | IllegalAccessException e) {
                System.out.println("Invalid field: " + key);
            }
        });

        Theatre updatedTheatre = theatreService.saveTheatre(theatre);
        return ResponseEntity.ok(updatedTheatre);
    }

    // Get all theatres
    @GetMapping
    public List<Theatre> getAllTheatres() {
        return theatreService.getAllTheatres();
    }

    // Get theatre by ID
    @GetMapping("/{id}")
    public Theatre getTheatreById(@PathVariable("id") Long id) {
        return theatreService.getTheatreById(id)
                .orElseThrow(() -> new RuntimeException("Theatre not found"));
    }
}
