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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final RoleRepository roleRepository;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper, RoleRepository roleRepository) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.roleRepository = roleRepository;
    }

    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        Customer customer = assignRoles(customerDTO, customerMapper.toEntity(customerDTO));

        Customer saved = customerRepository.save(customer);
        return customerMapper.toDTO(saved);
    }

    private Customer assignRoles(CustomerDTO customerDTO, Customer customer) {
        if (customerDTO.getRoles() != null && !customerDTO.getRoles().isEmpty()) {
            Set<Role> roles = customerDTO.getRoles().stream()
                    .map(roleName -> roleRepository.findByName(roleName)
                            .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName)))
                    .collect(Collectors.toSet());
            customer.setRoles(roles);
        }
        return customer;
    }

    public Optional<CustomerDTO> getCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(customerMapper::toDTO);
    }

    public CustomerDTO patchCustomer(CustomerDTO customerDTO, Map<String, Object> updates) {
        updates.forEach((key, value) -> {
            try {
                Field field = CustomerDTO.class.getDeclaredField(key);
                field.setAccessible(true);

                if (field.getType().equals(String.class)) {
                    field.set(customerDTO, value.toString());
                } else if (field.getType().equals(Set.class)) {
                    if (value instanceof List<?>) {
                        field.set(customerDTO, new HashSet<>((List<?>) value));
                    } else if (value instanceof String) {
                        field.set(customerDTO, Set.of((String) value));
                    } else {
                        throw new BadRequestException("Invalid value type for roles: " + value);
                    }
                } else {
                    field.set(customerDTO, value);
                }

            } catch (NoSuchFieldException | IllegalAccessException e) {
                log.warn("Invalid Customer field: {}", key);
                throw new BadRequestException("Invalid Customer field: " + key);
            }
        });

        return saveCustomer(customerDTO);
    }

    public PageResponse<CustomerDTO> getAllCustomers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customerPage = customerRepository.findAll(pageable);

        List<CustomerDTO> customerDTOs = customerMapper.toDTOList(customerPage.getContent());

        return new PageResponse<>(
                customerDTOs,
                customerPage.getNumber(),
                customerPage.getSize(),
                customerPage.getTotalElements(),
                customerPage.getTotalPages(),
                customerPage.isLast()
        );
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}
