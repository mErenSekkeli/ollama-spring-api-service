package org.erensekkeli.chatbotservice.service;

import org.erensekkeli.chatbotservice.entity.Customer;
import org.erensekkeli.chatbotservice.general.BaseEntityService;
import org.erensekkeli.chatbotservice.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService extends BaseEntityService<Customer, CustomerRepository> {
    protected CustomerService(CustomerRepository repository) {
        super(repository);
    }
}
