package com.company.mtbp.inventory.controller;

import com.company.mtbp.inventory.dto.SeatDTO;
import com.company.mtbp.inventory.service.SeatService;
import com.company.mtbp.shared.dto.PageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/seats")
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @PostMapping
    public ResponseEntity<SeatDTO> addSeat(@RequestBody SeatDTO seatDTO) {
        SeatDTO savedSeat = seatService.addSeat(seatDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSeat);
    }

    @DeleteMapping("/theatre/seat")
    public ResponseEntity<Void> deleteSeat(@RequestParam(required = false) Long theatreId,
                                           @RequestParam(required = false) String seatNumber,
                                           @RequestParam(required = false) Long seatId) {
        seatService.deleteSeat(seatId, theatreId, seatNumber);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/theatre/{theatreId}/seat/{seatId}")
    public ResponseEntity<SeatDTO> patchSeat(@PathVariable Long theatreId,
                                             @PathVariable Long seatId,
                                             @RequestBody Map<String, Object> updates) {
        SeatDTO updatedSeat = seatService.patchSeat(theatreId, seatId, updates);
        return ResponseEntity.ok(updatedSeat);
    }

    @GetMapping("/theatre")
    public ResponseEntity<PageResponse<SeatDTO>> getSeats(
            @RequestParam(required = false) Long theatreId,
            @RequestParam(required = false) String seatNumber,
            @RequestParam(required = false) Long seatId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageResponse<SeatDTO> result = seatService.getSeats(seatId, theatreId, seatNumber, page, size);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/search")
    public ResponseEntity<List<SeatDTO>> searchSeats(@RequestBody Map<String, Object> filters) {
        List<SeatDTO> seats = seatService.getSeatsByFilter(filters);
        return ResponseEntity.ok(seats);
    }
}
