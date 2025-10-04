package com.company.mtbp.partner.controller;

import com.company.mtbp.partner.request.SeatRequest;
import com.company.mtbp.partner.request.SeatUpdateRequest;
import com.company.mtbp.partner.request.ShowRequest;
import com.company.mtbp.partner.request.ShowUpdateRequest;
import com.company.mtbp.partner.service.TheatreSeatService;
import com.company.mtbp.partner.service.TheatreShowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/theatre-partner")
public class TheatrePartnerController {

    private final TheatreShowService theatreShowService;
    private final TheatreSeatService theatreSeatService;

    public TheatrePartnerController(TheatreShowService showService, TheatreSeatService theatreSeatService) {
        this.theatreShowService = showService;
        this.theatreSeatService = theatreSeatService;
    }

    @PostMapping("/shows")
    public ResponseEntity<String> createShow(@RequestBody ShowRequest request) {
        String createdShow = theatreShowService.createShow(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdShow);
    }

    @PatchMapping("/shows/{id}")
    public ResponseEntity<String> updateShow(
            @PathVariable Long id,
            @RequestBody ShowUpdateRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(theatreShowService.updateShow(id, request));
    }

    @DeleteMapping("/shows/{id}")
    public ResponseEntity<String> deleteShow(@PathVariable Long id) {
        theatreShowService.deleteShow(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("Show with ID " + id + " deleted successfully.");
    }

    @PostMapping("/shows/seats")
    public ResponseEntity<String> addSeat(@RequestBody SeatRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(theatreSeatService.addSeat(request));
    }

    @PatchMapping("/shows/theatre/{theatreId}/seat/{seatId}")
    public ResponseEntity<String> updateSeat(@PathVariable Long theatreId,
                                             @PathVariable Long seatId,
                                             @RequestBody SeatUpdateRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(theatreSeatService.updateSeat(theatreId, seatId, request));
    }
}
