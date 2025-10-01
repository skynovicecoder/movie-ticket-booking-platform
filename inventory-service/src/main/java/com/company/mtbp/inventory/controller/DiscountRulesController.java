package com.company.mtbp.inventory.controller;

import com.company.mtbp.inventory.dto.DiscountRulesDTO;
import com.company.mtbp.inventory.service.DiscountRulesService;
import com.company.mtbp.shared.dto.PageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/discounts")
public class DiscountRulesController {

    private final DiscountRulesService discountRulesService;

    public DiscountRulesController(DiscountRulesService discountRulesService) {
        this.discountRulesService = discountRulesService;
    }

    @PostMapping
    public ResponseEntity<DiscountRulesDTO> createDiscount(@RequestBody DiscountRulesDTO dto) {
        DiscountRulesDTO saved = discountRulesService.saveDiscount(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<DiscountRulesDTO> patchDiscount(@PathVariable Long id,
                                                          @RequestBody Map<String, Object> updates) {
        Optional<DiscountRulesDTO> optional = discountRulesService.getDiscountById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        DiscountRulesDTO updated = discountRulesService.patchDiscount(optional.get(), updates);
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public ResponseEntity<PageResponse<DiscountRulesDTO>> getAllDiscounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageResponse<DiscountRulesDTO> response = discountRulesService.getAllDiscounts(page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiscountRulesDTO> getDiscountById(@PathVariable Long id) {
        return discountRulesService.getDiscountById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/offers")
    public ResponseEntity<List<DiscountRulesDTO>> getOffers(
            @RequestParam(required = false) Long cityId,
            @RequestParam(required = false) Long theatreId) {

        List<DiscountRulesDTO> offers = discountRulesService.getOffers(cityId, theatreId);

        if (offers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(offers);
    }
}
