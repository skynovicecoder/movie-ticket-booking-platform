package com.company.mtbp.inventory.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleDTOTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        RoleDTO dto = new RoleDTO(1L, "ADMIN");

        assertEquals(1L, dto.getId());
        assertEquals("ADMIN", dto.getName());
    }

    @Test
    void testSetters() {
        RoleDTO dto = new RoleDTO();
        dto.setId(2L);
        dto.setName("USER");

        assertEquals(2L, dto.getId());
        assertEquals("USER", dto.getName());
    }

    @Test
    void testBuilder() {
        RoleDTO dto = RoleDTO.builder()
                .id(3L)
                .name("MODERATOR")
                .build();

        assertEquals(3L, dto.getId());
        assertEquals("MODERATOR", dto.getName());
    }

    @Test
    void testEqualsAndHashCode() {
        RoleDTO dto1 = new RoleDTO(4L, "GUEST");
        RoleDTO dto2 = new RoleDTO(4L, "GUEST");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        RoleDTO dto = new RoleDTO(5L, "TEST");
        String str = dto.toString();

        assertNotNull(str);
        assertTrue(str.contains("5"));
        assertTrue(str.contains("TEST"));
    }
}
