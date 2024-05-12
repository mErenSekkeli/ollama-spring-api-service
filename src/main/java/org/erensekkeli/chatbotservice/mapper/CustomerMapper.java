package org.erensekkeli.chatbotservice.mapper;

import org.erensekkeli.chatbotservice.dto.CustomerDTO;
import org.erensekkeli.chatbotservice.dto.CustomerSaveRequest;
import org.erensekkeli.chatbotservice.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    CustomerDTO convertToCustomerDTO(Customer customer);
    List<CustomerDTO> convertToCustomerDTOs(List<Customer> customers);

    Customer convertToCustomer(CustomerSaveRequest request);
}
