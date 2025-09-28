package com.company.mtbp.inventory.service;

import com.company.mtbp.inventory.dto.CustomerDTO;
import com.company.mtbp.inventory.entity.Customer;
import com.company.mtbp.inventory.exception.BadRequestException;
import com.company.mtbp.inventory.mapper.CustomerMapper;
import com.company.mtbp.inventory.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        Customer customer = customerMapper.toEntity(customerDTO);
        Customer saved = customerRepository.save(customer);
        return customerMapper.toDTO(saved);
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

    public List<CustomerDTO> getAllCustomers() {
        return customerMapper.toDTOList(customerRepository.findAll());
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}
