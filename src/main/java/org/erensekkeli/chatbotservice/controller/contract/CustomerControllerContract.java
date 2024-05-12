package org.erensekkeli.chatbotservice.controller.contract;

import org.erensekkeli.chatbotservice.dto.CustomerDTO;
import org.erensekkeli.chatbotservice.dto.CustomerSaveRequest;

import java.util.List;

public interface CustomerControllerContract {
    List<CustomerDTO> getAllCustomers();

    CustomerDTO getCustomerById(Long id);

    CustomerDTO saveCustomer(CustomerSaveRequest customerDTO);
}
