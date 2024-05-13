package org.erensekkeli.chatbotservice.controller.contract.impl;

import lombok.RequiredArgsConstructor;
import org.erensekkeli.chatbotservice.controller.contract.CustomerControllerContract;
import org.erensekkeli.chatbotservice.dto.CustomerDTO;
import org.erensekkeli.chatbotservice.dto.CustomerSaveRequest;
import org.erensekkeli.chatbotservice.dto.CustomerUpdateRequest;
import org.erensekkeli.chatbotservice.entity.Customer;
import org.erensekkeli.chatbotservice.exceptions.ItemNotFoundException;
import org.erensekkeli.chatbotservice.mapper.CustomerMapper;
import org.erensekkeli.chatbotservice.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerControllerContractImpl implements CustomerControllerContract {

    private final CustomerService customerService;

    @Override
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customerList = customerService.findAll();

        return CustomerMapper.INSTANCE.convertToCustomerDTOs(customerList);
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerService.findByIdWithControl(id);

        return CustomerMapper.INSTANCE.convertToCustomerDTO(customer);
    }

    @Override
    public CustomerDTO saveCustomer(CustomerSaveRequest request) {
        Customer customer = CustomerMapper.INSTANCE.convertToCustomer(request);

        customer = customerService.save(customer);

        return CustomerMapper.INSTANCE.convertToCustomerDTO(customer);
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerUpdateRequest request) {
        Customer customer = customerService.findByIdWithControl(id);

        CustomerMapper.INSTANCE.updateCustomerFields(customer, request);

        customer = customerService.save(customer);

        return CustomerMapper.INSTANCE.convertToCustomerDTO(customer);
    }

    @Override
    public void deleteCustomer(Long id) {
        Optional<Customer> customer = customerService.findById(id);
        if (customer.isEmpty()) {
            throw new ItemNotFoundException("Customer not found with id: " + id + " for delete operation.");
        }
        customerService.delete(id);
    }
}
