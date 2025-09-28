package com.company.mtbp.inventory.mapper;

import com.company.mtbp.inventory.dto.CustomerDTO;
import com.company.mtbp.inventory.entity.Customer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerDTO toDTO(Customer customer);

    Customer toEntity(CustomerDTO dto);

    List<CustomerDTO> toDTOList(List<Customer> customers);

    List<Customer> toEntityList(List<CustomerDTO> dtos);
}
