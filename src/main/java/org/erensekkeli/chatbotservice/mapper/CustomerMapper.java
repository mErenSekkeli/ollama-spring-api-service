package org.erensekkeli.chatbotservice.mapper;

import org.erensekkeli.chatbotservice.dto.CustomerDTO;
import org.erensekkeli.chatbotservice.dto.CustomerSaveRequest;
import org.erensekkeli.chatbotservice.dto.CustomerUpdateRequest;
import org.erensekkeli.chatbotservice.entity.Customer;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = "spring")
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    CustomerDTO convertToCustomerDTO(Customer customer);
    List<CustomerDTO> convertToCustomerDTOs(List<Customer> customers);

    Customer convertToCustomer(CustomerSaveRequest request);

    @Mapping(target = "id", ignore = true)
    void updateCustomerFields(@MappingTarget Customer customer, CustomerUpdateRequest request);
}
