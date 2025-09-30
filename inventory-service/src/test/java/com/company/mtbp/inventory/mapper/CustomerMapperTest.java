package com.company.mtbp.inventory.mapper;

import com.company.mtbp.inventory.dto.CustomerDTO;
import com.company.mtbp.inventory.entity.Customer;
import com.company.mtbp.inventory.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomerMapperTest {

    private CustomerMapper customerMapper;

    @BeforeEach
    void setup() {
        customerMapper = Mappers.getMapper(CustomerMapper.class);
    }

    @Test
    void toDTO_shouldMapCustomerToCustomerDTO_withRoles() {
        Role role1 = new Role();
        role1.setName("ADMIN");
        Role role2 = new Role();
        role2.setName("USER");

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");
        customer.setRoles(Set.of(role1, role2));

        CustomerDTO dto = customerMapper.toDTO(customer);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("John Doe", dto.getName());
        assertNotNull(dto.getRoles());
        assertTrue(dto.getRoles().contains("ADMIN"));
        assertTrue(dto.getRoles().contains("USER"));
    }

    @Test
    void toEntity_shouldMapCustomerDTOToCustomer_ignoringRoles() {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(2L);
        dto.setName("Jane Doe");
        dto.setRoles(Set.of("ADMIN", "USER"));

        Customer customer = customerMapper.toEntity(dto);

        assertNotNull(customer);
        assertEquals(2L, customer.getId());
        assertEquals("Jane Doe", customer.getName());
        assertNull(customer.getRoles());
    }

    @Test
    void toDTOList_shouldMapCustomerListToDTOList() {
        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setName("John");

        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setName("Jane");

        List<CustomerDTO> dtoList = customerMapper.toDTOList(List.of(customer1, customer2));

        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());
        assertEquals("John", dtoList.get(0).getName());
        assertEquals("Jane", dtoList.get(1).getName());
    }

    @Test
    void toEntityList_shouldMapDTOListToEntityList() {
        CustomerDTO dto1 = new CustomerDTO();
        dto1.setId(1L);
        dto1.setName("John");

        CustomerDTO dto2 = new CustomerDTO();
        dto2.setId(2L);
        dto2.setName("Jane");

        List<Customer> customerList = customerMapper.toEntityList(List.of(dto1, dto2));

        assertNotNull(customerList);
        assertEquals(2, customerList.size());
        assertEquals("John", customerList.get(0).getName());
        assertEquals("Jane", customerList.get(1).getName());
    }

    @Test
    void mapRolesToStrings_shouldConvertRolesToStringSet() {
        Role role1 = new Role();
        role1.setName("ADMIN");
        Role role2 = new Role();
        role2.setName("USER");

        Set<String> result = customerMapper.mapRolesToStrings(Set.of(role1, role2));

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("ADMIN"));
        assertTrue(result.contains("USER"));
    }

    @Test
    void mapRolesToStrings_shouldReturnNullForNullInput() {
        Set<String> result = customerMapper.mapRolesToStrings(null);
        assertNull(result);
    }
}
