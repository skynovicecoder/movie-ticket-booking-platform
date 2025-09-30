package com.company.mtbp.inventory.controller;

import com.company.mtbp.inventory.dto.CustomerDTO;
import com.company.mtbp.inventory.service.CustomerService;
import com.github.javafaker.Faker;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
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

        mockMvc.perform(post("/api/customers")
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

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleCustomer.getId()))
                .andExpect(jsonPath("$.name").value(sampleCustomer.getName()));
    }

    @Test
    void getCustomerById_returnsNotFound_whenCustomerMissing() throws Exception {
        Mockito.when(customerService.getCustomerById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/customers/99"))
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

        mockMvc.perform(patch("/api/customers/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"UpdatedName\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UpdatedName"));
    }

    @Test
    void patchCustomer_returnsNotFound_whenCustomerMissing() throws Exception {
        Mockito.when(customerService.getCustomerById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(patch("/api/customers/update/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"UpdatedName\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllCustomers_returnsList() throws Exception {
        List<CustomerDTO> customers = List.of(sampleCustomer);
        Mockito.when(customerService.getAllCustomers()).thenReturn(customers);

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(customers.size()))
                .andExpect(jsonPath("$[0].email").value(sampleCustomer.getEmail()));
    }

    @Test
    void deleteCustomer_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/customers/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(customerService).deleteCustomer(1L);
    }
}
