package com.company.mtbp.inventory.controller;


import com.company.mtbp.inventory.dto.CustomerDTO;
import com.company.mtbp.inventory.pagedto.PageResponse;
import com.company.mtbp.inventory.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customerDTO) {
        CustomerDTO savedCustomer = customerService.saveCustomer(customerDTO);
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<CustomerDTO> patchCustomer(@PathVariable("id") Long id,
                                                     @RequestBody Map<String, Object> updates) {
        Optional<CustomerDTO> optionalCustomer = customerService.getCustomerById(id);
        if (optionalCustomer.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        CustomerDTO customerDTO = optionalCustomer.get();
        CustomerDTO updatedCustomer = customerService.patchCustomer(customerDTO, updates);

        return ResponseEntity.ok(updatedCustomer);
    }

    @GetMapping
    public ResponseEntity<PageResponse<CustomerDTO>> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageResponse<CustomerDTO> response = customerService.getAllCustomers(page, size);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
