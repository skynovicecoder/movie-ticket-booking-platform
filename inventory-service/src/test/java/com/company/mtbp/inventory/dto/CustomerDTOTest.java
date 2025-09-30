package com.company.mtbp.inventory.dto;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomerDTOTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        CustomerDTO customer = new CustomerDTO(1L, "John Doe", "john@example.com", "1234567890", Set.of("USER", "ADMIN"));

        assertEquals(1L, customer.getId());
        assertEquals("John Doe", customer.getName());
        assertEquals("john@example.com", customer.getEmail());
        assertEquals("1234567890", customer.getPhone());
        assertEquals(Set.of("USER", "ADMIN"), customer.getRoles());
    }

    @Test
    void testSetters() {
        CustomerDTO customer = new CustomerDTO();
        customer.setId(2L);
        customer.setName("Jane Doe");
        customer.setEmail("jane@example.com");
        customer.setPhone("0987654321");
        customer.setRoles(Set.of("USER"));

        assertEquals(2L, customer.getId());
        assertEquals("Jane Doe", customer.getName());
        assertEquals("jane@example.com", customer.getEmail());
        assertEquals("0987654321", customer.getPhone());
        assertEquals(Set.of("USER"), customer.getRoles());
    }

    @Test
    void testBuilder() {
        CustomerDTO customer = CustomerDTO.builder()
                .id(3L)
                .name("Alice")
                .email("alice@example.com")
                .phone("1112223333")
                .roles(Set.of("ADMIN"))
                .build();

        assertEquals(3L, customer.getId());
        assertEquals("Alice", customer.getName());
        assertEquals("alice@example.com", customer.getEmail());
        assertEquals("1112223333", customer.getPhone());
        assertEquals(Set.of("ADMIN"), customer.getRoles());
    }
    
}
