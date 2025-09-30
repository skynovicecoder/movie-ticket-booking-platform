package com.company.mtbp.inventory.entity;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    @Test
    void testRoleBuilderAndGetters() {
        Role role = Role.builder()
                .id(1L)
                .name("ADMIN")
                .users(new HashSet<>())
                .build();

        assertEquals(1L, role.getId());
        assertEquals("ADMIN", role.getName());
        assertNotNull(role.getUsers());
        assertTrue(role.getUsers().isEmpty());
    }

    @Test
    void testSettersAndGetters() {
        Role role = new Role();
        role.setId(2L);
        role.setName("CUSTOMER");

        assertEquals(2L, role.getId());
        assertEquals("CUSTOMER", role.getName());
    }

    @Test
    void testRoleUsersRelationship() {
        Role role = new Role();
        role.setId(3L);
        role.setName("THEATRE_OWNER");

        Customer customer = Customer.builder()
                .id(101L)
                .name("Alice")
                .email("alice@example.com")
                .phone("1234567890")
                .roles(new HashSet<>())
                .build();

        Set<Customer> customers = new HashSet<>();
        customers.add(customer);

        role.setUsers(customers);

        assertNotNull(role.getUsers());
        assertEquals(1, role.getUsers().size());
        assertTrue(role.getUsers().contains(customer));
    }

    @Test
    void testNoArgsConstructor() {
        Role role = new Role();
        assertNull(role.getId());
        assertNull(role.getName());
        assertNull(role.getUsers());
    }

    @Test
    void testAllArgsConstructor() {
        Set<Customer> users = new HashSet<>();
        Role role = new Role(5L, "ADMIN", users);

        assertEquals(5L, role.getId());
        assertEquals("ADMIN", role.getName());
        assertEquals(users, role.getUsers());
    }
}
