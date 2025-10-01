package com.company.mtbp.inventory.controller;

import com.company.mtbp.inventory.dto.CityDTO;
import com.company.mtbp.inventory.pagedto.PageResponse;
import com.company.mtbp.inventory.service.CityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/cities")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @PostMapping
    public ResponseEntity<CityDTO> createCity(@RequestBody CityDTO cityDTO) {
        CityDTO responseDTO = cityService.saveCity(cityDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<PageResponse<CityDTO>> getAllCities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageResponse<CityDTO> response = cityService.getAllCities(page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityDTO> getCityById(@PathVariable("id") Long id) {
        Optional<CityDTO> city = cityService.getCityById(id);
        return city.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<CityDTO> patchCity(@PathVariable("id") Long id,
                                             @RequestBody Map<String, Object> updates) {

        Optional<CityDTO> optionalCity = cityService.getCityById(id);
        if (optionalCity.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        CityDTO cityDTO = optionalCity.get();
        CityDTO updatedCity = cityService.patchCity(cityDTO, updates);

        return ResponseEntity.ok(updatedCity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable("id") Long id) {
        cityService.deleteCity(id);
        return ResponseEntity.noContent().build();
    }

}
