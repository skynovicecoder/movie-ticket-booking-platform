package com.company.mtbp.inventory.controller;

import com.company.mtbp.inventory.dto.CustomerDTO;
import com.company.mtbp.inventory.service.CustomerService;
import com.company.mtbp.shared.dto.PageResponse;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private MockMvc mockMvc;
    private CustomerDTO sampleCustomer;

    @BeforeEach
    void setup() {
        Faker faker = new Faker();
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();

        sampleCustomer = new CustomerDTO();
        sampleCustomer.setId(1L);
        sampleCustomer.setName(faker.name().fullName());
        sampleCustomer.setEmail(faker.internet().emailAddress());
        sampleCustomer.setPhone(faker.phoneNumber().cellPhone());
    }

    @Test
    void createCustomer_returnsCreatedCustomer() throws Exception {
        Mockito.when(customerService.saveCustomer(any(CustomerDTO.class))).thenReturn(sampleCustomer);

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"" + sampleCustomer.getName() + "\", " +
                                "\"email\":\"" + sampleCustomer.getEmail() + "\", " +
                                "\"phone\":\"" + sampleCustomer.getPhone() + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(sampleCustomer.getId()))
                .andExpect(jsonPath("$.name").value(sampleCustomer.getName()))
                .andExpect(jsonPath("$.email").value(sampleCustomer.getEmail()));
    }

    @Test
    void getCustomerById_returnsCustomer() throws Exception {
        Mockito.when(customerService.getCustomerById(1L)).thenReturn(Optional.of(sampleCustomer));

        mockMvc.perform(get("/api/v1/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleCustomer.getId()))
                .andExpect(jsonPath("$.name").value(sampleCustomer.getName()));
    }

    @Test
    void getCustomerById_returnsNotFound_whenCustomerMissing() throws Exception {
        Mockito.when(customerService.getCustomerById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/customers/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void patchCustomer_updatesCustomer() throws Exception {
        CustomerDTO updated = new CustomerDTO();
        updated.setId(sampleCustomer.getId());
        updated.setName("UpdatedName");
        updated.setEmail(sampleCustomer.getEmail());
        updated.setPhone(sampleCustomer.getPhone());

        Mockito.when(customerService.getCustomerById(1L)).thenReturn(Optional.of(sampleCustomer));
        Mockito.when(customerService.patchCustomer(any(CustomerDTO.class), any(Map.class))).thenReturn(updated);

        mockMvc.perform(patch("/api/v1/customers/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"UpdatedName\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UpdatedName"));
    }

    @Test
    void patchCustomer_returnsNotFound_whenCustomerMissing() throws Exception {
        Mockito.when(customerService.getCustomerById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(patch("/api/v1/customers/update/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"UpdatedName\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllCustomers_returnsPaginatedResponse() throws Exception {
        PageResponse<CustomerDTO> response = new PageResponse<>(
                List.of(sampleCustomer),
                0, 10,
                1L,
                1,
                true
        );

        Mockito.when(customerService.getAllCustomers(0, 10)).thenReturn(response);

        mockMvc.perform(get("/api/v1/customers")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].email").value(sampleCustomer.getEmail()))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.pageNumber").value(0))
                .andExpect(jsonPath("$.last").value(true));
    }

    @Test
    void deleteCustomer_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/customers/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(customerService).deleteCustomer(1L);
    }
}
