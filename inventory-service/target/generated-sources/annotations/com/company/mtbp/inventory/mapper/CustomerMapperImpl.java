package com.company.mtbp.inventory.mapper;

import com.company.mtbp.inventory.dto.CustomerDTO;
import com.company.mtbp.inventory.entity.Customer;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-28T20:32:12+0530",
    comments = "version: 1.6.0.Beta2, compiler: javac, environment: Java 21 (Eclipse Adoptium)"
)
@Component
public class CustomerMapperImpl implements CustomerMapper {

    @Override
    public CustomerDTO toDTO(Customer customer) {
        if ( customer == null ) {
            return null;
        }

        CustomerDTO customerDTO = new CustomerDTO();

        customerDTO.setId( customer.getId() );
        customerDTO.setName( customer.getName() );
        customerDTO.setEmail( customer.getEmail() );
        customerDTO.setPhone( customer.getPhone() );

        return customerDTO;
    }

    @Override
    public Customer toEntity(CustomerDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Customer customer = new Customer();

        customer.setId( dto.getId() );
        customer.setName( dto.getName() );
        customer.setEmail( dto.getEmail() );
        customer.setPhone( dto.getPhone() );

        return customer;
    }

    @Override
    public List<CustomerDTO> toDTOList(List<Customer> customers) {
        if ( customers == null ) {
            return null;
        }

        List<CustomerDTO> list = new ArrayList<CustomerDTO>( customers.size() );
        for ( Customer customer : customers ) {
            list.add( toDTO( customer ) );
        }

        return list;
    }

    @Override
    public List<Customer> toEntityList(List<CustomerDTO> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<Customer> list = new ArrayList<Customer>( dtos.size() );
        for ( CustomerDTO customerDTO : dtos ) {
            list.add( toEntity( customerDTO ) );
        }

        return list;
    }
}
