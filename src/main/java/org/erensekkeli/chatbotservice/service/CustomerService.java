package org.erensekkeli.chatbotservice.service;

import org.erensekkeli.chatbotservice.entity.Customer;
import org.erensekkeli.chatbotservice.general.BaseEntityService;
import org.erensekkeli.chatbotservice.repository.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService extends BaseEntityService<Customer, CustomerRepository> {

    private final PasswordEncoder passwordEncoder;

    protected CustomerService(CustomerRepository repository, PasswordEncoder passwordEncoder) {
        super(repository);
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Customer save(Customer entity) {
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        return getRepository().save(entity);
    }

    public Optional<Customer> findByUsername(String username) {
        return getRepository().findByUsername(username);
    }
}
