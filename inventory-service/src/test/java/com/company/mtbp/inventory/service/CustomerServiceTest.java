package com.company.mtbp.inventory.service;

import com.company.mtbp.inventory.dto.CustomerDTO;
import com.company.mtbp.inventory.entity.Customer;
import com.company.mtbp.inventory.entity.Role;
import com.company.mtbp.inventory.exception.BadRequestException;
import com.company.mtbp.inventory.exception.ResourceNotFoundException;
import com.company.mtbp.inventory.mapper.CustomerMapper;
import com.company.mtbp.inventory.repository.CustomerRepository;
import com.company.mtbp.inventory.repository.RoleRepository;
import com.company.mtbp.shared.dto.PageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer sampleCustomer;
    private CustomerDTO sampleCustomerDTO;
    private Role sampleRole;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleRole = Role.builder()
                .id(1L)
                .name("USER")
                .build();

        sampleCustomer = Customer.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .roles(Set.of(sampleRole))
                .build();

        sampleCustomerDTO = CustomerDTO.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .roles(Set.of("USER"))
                .build();
    }

    @Test
    void saveCustomer_success_withRoles() {
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(sampleRole));
        when(customerMapper.toEntity(sampleCustomerDTO)).thenReturn(sampleCustomer);
        when(customerRepository.save(sampleCustomer)).thenReturn(sampleCustomer);
        when(customerMapper.toDTO(sampleCustomer)).thenReturn(sampleCustomerDTO);

        CustomerDTO result = customerService.saveCustomer(sampleCustomerDTO);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(customerRepository).save(sampleCustomer);
    }

    @Test
    void saveCustomer_roleNotFound_throwsException() {
        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.empty());

        CustomerDTO dto = CustomerDTO.builder()
                .roles(Set.of("ADMIN"))
                .build();

        assertThrows(ResourceNotFoundException.class, () -> customerService.saveCustomer(dto));
    }

    @Test
    void getCustomerById_found() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(sampleCustomer));
        when(customerMapper.toDTO(sampleCustomer)).thenReturn(sampleCustomerDTO);

        Optional<CustomerDTO> result = customerService.getCustomerById(1L);

        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
    }

    @Test
    void getCustomerById_notFound() {
        when(customerRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<CustomerDTO> result = customerService.getCustomerById(2L);

        assertTrue(result.isEmpty());
    }

    @Test
    void patchCustomer_updatesNameSuccessfully() {
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(sampleRole));
        when(customerMapper.toEntity(any())).thenReturn(sampleCustomer);
        when(customerRepository.save(any())).thenReturn(sampleCustomer);
        when(customerMapper.toDTO(any())).thenReturn(sampleCustomerDTO);

        Map<String, Object> updates = Map.of("name", "Jane Doe");

        CustomerDTO updated = customerService.patchCustomer(sampleCustomerDTO, updates);

        assertNotNull(updated);
        verify(customerRepository).save(any());
    }

    @Test
    void patchCustomer_invalidField_throwsBadRequest() {
        assertThrows(BadRequestException.class, () -> {
            customerService.patchCustomer(sampleCustomerDTO, Map.of("invalidField", "value"));
        });
    }

    @Test
    void getAllCustomers_success() {
        Page<Customer> customerPage = new PageImpl<>(List.of(sampleCustomer));
        when(customerRepository.findAll(PageRequest.of(0, 10))).thenReturn(customerPage);
        when(customerMapper.toDTOList(customerPage.getContent())).thenReturn(List.of(sampleCustomerDTO));

        PageResponse<CustomerDTO> result = customerService.getAllCustomers(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("John Doe", result.getContent().getFirst().getName());
        assertEquals(sampleCustomerDTO.getEmail(), result.getContent().getFirst().getEmail());
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(0, result.getPageNumber());
        assertTrue(result.isLast());
    }

    @Test
    void deleteCustomer_callsRepository() {
        customerService.deleteCustomer(1L);
        verify(customerRepository).deleteById(1L);
    }
}
