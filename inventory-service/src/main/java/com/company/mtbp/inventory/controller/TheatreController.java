package com.company.mtbp.inventory.controller;

import com.company.mtbp.inventory.dto.TheatreDTO;
import com.company.mtbp.inventory.service.TheatreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/theatres")
public class TheatreController {

    private final TheatreService theatreService;

    public TheatreController(TheatreService theatreService) {
        this.theatreService = theatreService;
    }

    @PostMapping
    public ResponseEntity<TheatreDTO> createTheatre(@RequestBody TheatreDTO theatreDTO) {
        TheatreDTO savedTheatre = theatreService.saveTheatre(theatreDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTheatre);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<TheatreDTO> patchTheatre(@PathVariable("id") Long id,
                                                   @RequestBody Map<String, Object> updates) {
        Optional<TheatreDTO> optionalTheatre = theatreService.getTheatreById(id);
        if (optionalTheatre.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        TheatreDTO theatre = optionalTheatre.get();
        TheatreDTO updatedTheatre = theatreService.patchTheatre(theatre, updates);
        return ResponseEntity.ok(updatedTheatre);
    }

    @GetMapping
    public ResponseEntity<List<TheatreDTO>> getAllTheatres() {
        List<TheatreDTO> theatres = theatreService.getAllTheatres();
        if (theatres.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(theatres);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TheatreDTO> getTheatreById(@PathVariable Long id) {
        Optional<TheatreDTO> theatre = theatreService.getTheatreById(id);
        return theatre.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/city/{cityName}")
    public ResponseEntity<List<TheatreDTO>> getTheatresByCity(@PathVariable String cityName) {
        List<TheatreDTO> theatres = theatreService.getTheatresByCity(cityName);
        if (theatres.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(theatres);
    }
}
