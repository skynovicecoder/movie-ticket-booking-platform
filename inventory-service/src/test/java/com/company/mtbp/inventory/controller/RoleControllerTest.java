package com.company.mtbp.inventory.controller;

import com.company.mtbp.inventory.dto.RoleDTO;
import com.company.mtbp.inventory.service.RoleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class RoleControllerTest {

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private RoleDTO sampleRole;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(roleController).build();
        objectMapper = new ObjectMapper();

        sampleRole = new RoleDTO();
        sampleRole.setId(1L);
        sampleRole.setName("ADMIN");
    }

    @Test
    void createRole_returnsCreatedRole() throws Exception {
        Mockito.when(roleService.createRole(any(RoleDTO.class))).thenReturn(sampleRole);

        mockMvc.perform(post("/api/admin/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRole)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(sampleRole.getId()))
                .andExpect(jsonPath("$.name").value(sampleRole.getName()));
    }

    @Test
    void getRoleById_returnsRole() throws Exception {
        Mockito.when(roleService.getRoleById(1L)).thenReturn(sampleRole);

        mockMvc.perform(get("/api/admin/roles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleRole.getId()))
                .andExpect(jsonPath("$.name").value(sampleRole.getName()));
    }

    @Test
    void getAllRoles_returnsListOfRoles() throws Exception {
        List<RoleDTO> roles = List.of(sampleRole);
        Mockito.when(roleService.getAllRoles()).thenReturn(roles);

        mockMvc.perform(get("/api/admin/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(roles.size()))
                .andExpect(jsonPath("$[0].name").value(sampleRole.getName()));
    }

    @Test
    void patchRole_updatesRole() throws Exception {
        RoleDTO updatedRole = new RoleDTO();
        updatedRole.setId(sampleRole.getId());
        updatedRole.setName("UPDATED");

        Mockito.when(roleService.patchRole(eq(1L), any(Map.class))).thenReturn(updatedRole);

        mockMvc.perform(patch("/api/admin/roles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"UPDATED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UPDATED"));
    }

    @Test
    void deleteRole_returnsNoContent() throws Exception {
        Mockito.doNothing().when(roleService).deleteRole(1L);

        mockMvc.perform(delete("/api/admin/roles/1"))
                .andExpect(status().isNoContent());
    }
}
