package com.company.mtbp.inventory.entity;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        Customer customer = new Customer();
        Booking booking = new Booking();
        Role role = new Role();

        customer.setId(1L);
        customer.setName("John Doe");
        customer.setEmail("john@example.com");
        customer.setPhone("1234567890");
        customer.setBookings(List.of(booking));
        customer.setRoles(Set.of(role));

        assertThat(customer.getId()).isEqualTo(1L);
        assertThat(customer.getName()).isEqualTo("John Doe");
        assertThat(customer.getEmail()).isEqualTo("john@example.com");
        assertThat(customer.getPhone()).isEqualTo("1234567890");
        assertThat(customer.getBookings()).containsExactly(booking);
        assertThat(customer.getRoles()).containsExactly(role);
    }

    @Test
    void testAllArgsConstructor() {
        Booking booking = new Booking();
        Role role = new Role();

        Customer customer = new Customer(
                2L,
                "Alice Smith",
                "alice@example.com",
                "9876543210",
                List.of(booking),
                Set.of(role)
        );

        assertThat(customer.getId()).isEqualTo(2L);
        assertThat(customer.getName()).isEqualTo("Alice Smith");
        assertThat(customer.getEmail()).isEqualTo("alice@example.com");
        assertThat(customer.getPhone()).isEqualTo("9876543210");
        assertThat(customer.getBookings()).containsExactly(booking);
        assertThat(customer.getRoles()).containsExactly(role);
    }

    @Test
    void testBuilder() {
        Booking booking = new Booking();
        Role role = new Role();

        Customer customer = Customer.builder()
                .id(3L)
                .name("Bob Marley")
                .email("bob@example.com")
                .phone("5551234567")
                .bookings(List.of(booking))
                .roles(Set.of(role))
                .build();

        assertThat(customer.getId()).isEqualTo(3L);
        assertThat(customer.getName()).isEqualTo("Bob Marley");
        assertThat(customer.getEmail()).isEqualTo("bob@example.com");
        assertThat(customer.getPhone()).isEqualTo("5551234567");
        assertThat(customer.getBookings()).containsExactly(booking);
        assertThat(customer.getRoles()).containsExactly(role);
    }

}
