package com.company.mtbp.inventory.controller;

import com.company.mtbp.inventory.dto.RoleDTO;
import com.company.mtbp.inventory.pagedto.PageResponse;
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

        mockMvc.perform(post("/api/v1/admin/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRole)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(sampleRole.getId()))
                .andExpect(jsonPath("$.name").value(sampleRole.getName()));
    }

    @Test
    void getRoleById_returnsRole() throws Exception {
        Mockito.when(roleService.getRoleById(1L)).thenReturn(sampleRole);

        mockMvc.perform(get("/api/v1/admin/roles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleRole.getId()))
                .andExpect(jsonPath("$.name").value(sampleRole.getName()));
    }

    @Test
    void getAllRoles_returnsPaginatedResponse() throws Exception {
        PageResponse<RoleDTO> response = new PageResponse<>(
                List.of(sampleRole),
                0, 10,
                1L,
                1,
                true
        );

        Mockito.when(roleService.getAllRoles(0, 10)).thenReturn(response);

        mockMvc.perform(get("/api/v1/admin/roles")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value(sampleRole.getName()))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.pageNumber").value(0))
                .andExpect(jsonPath("$.last").value(true));
    }

    @Test
    void patchRole_updatesRole() throws Exception {
        RoleDTO updatedRole = new RoleDTO();
        updatedRole.setId(sampleRole.getId());
        updatedRole.setName("UPDATED");

        Mockito.when(roleService.patchRole(eq(1L), any(Map.class))).thenReturn(updatedRole);

        mockMvc.perform(patch("/api/v1/admin/roles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"UPDATED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UPDATED"));
    }

    @Test
    void deleteRole_returnsNoContent() throws Exception {
        Mockito.doNothing().when(roleService).deleteRole(1L);

        mockMvc.perform(delete("/api/v1/admin/roles/1"))
                .andExpect(status().isNoContent());
    }
}
