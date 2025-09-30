package com.company.mtbp.inventory.service;

import com.company.mtbp.inventory.dto.RoleDTO;
import com.company.mtbp.inventory.entity.Role;
import com.company.mtbp.inventory.exception.BadRequestException;
import com.company.mtbp.inventory.exception.ResourceNotFoundException;
import com.company.mtbp.inventory.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    private Role sampleRole;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleRole = Role.builder()
                .id(1L)
                .name("ADMIN")
                .build();

        RoleDTO sampleRoleDTO = RoleDTO.builder()
                .id(1L)
                .name("ADMIN")
                .build();
    }

    @Test
    void createRole_success() {
        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.empty());
        when(roleRepository.save(any(Role.class))).thenReturn(sampleRole);

        RoleDTO result = roleService.createRole(RoleDTO.builder().name("ADMIN").build());

        assertNotNull(result);
        assertEquals("ADMIN", result.getName());
        verify(roleRepository).save(any(Role.class));
    }

    @Test
    void createRole_existingRole_throwsBadRequest() {
        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(sampleRole));

        assertThrows(BadRequestException.class, () ->
                roleService.createRole(RoleDTO.builder().name("ADMIN").build())
        );
    }

    @Test
    void getRoleById_success() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(sampleRole));

        RoleDTO result = roleService.getRoleById(1L);

        assertNotNull(result);
        assertEquals("ADMIN", result.getName());
    }

    @Test
    void getRoleById_notFound_throwsResourceNotFound() {
        when(roleRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> roleService.getRoleById(2L));
    }

    @Test
    void getAllRoles_success() {
        when(roleRepository.findAll()).thenReturn(List.of(sampleRole));

        List<RoleDTO> result = roleService.getAllRoles();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ADMIN", result.getFirst().getName());
    }

    @Test
    void patchRole_success() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(sampleRole));
        when(roleRepository.save(any(Role.class))).thenReturn(sampleRole);

        RoleDTO patched = roleService.patchRole(1L, Map.of("name", "SUPERADMIN"));

        assertNotNull(patched);
        verify(roleRepository).save(any(Role.class));
    }

    @Test
    void patchRole_invalidField_throwsBadRequest() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(sampleRole));

        assertThrows(BadRequestException.class, () -> roleService.patchRole(1L, Map.of("invalidField", "value")));
    }

    @Test
    void deleteRole_success() {
        when(roleRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> roleService.deleteRole(1L));
        verify(roleRepository).deleteById(1L);
    }

    @Test
    void deleteRole_notFound_throwsResourceNotFound() {
        when(roleRepository.existsById(2L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> roleService.deleteRole(2L));
    }
}
