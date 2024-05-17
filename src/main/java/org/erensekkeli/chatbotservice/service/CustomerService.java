package org.erensekkeli.chatbotservice.service;

import jakarta.annotation.PostConstruct;
import org.erensekkeli.chatbotservice.entity.Customer;
import org.erensekkeli.chatbotservice.entity.Role;
import org.erensekkeli.chatbotservice.enums.EnumRole;
import org.erensekkeli.chatbotservice.general.BaseEntityService;
import org.erensekkeli.chatbotservice.repository.CustomerRepository;
import org.erensekkeli.chatbotservice.repository.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService extends BaseEntityService<Customer, CustomerRepository> {

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    protected CustomerService(CustomerRepository repository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        super(repository);
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public Customer save(Customer entity) {
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        return getRepository().save(entity);
    }

    public Optional<Customer> findByUsername(String username) {
        return getRepository().findByUsername(username);
    }

    public Optional<Role> findRoleByName(EnumRole name) {
        return roleRepository.findByName(name);
    }

    @PostConstruct
    private void initializeRoles() {
        addRole(EnumRole.ROLE_USER);
        addRole(EnumRole.ROLE_ADMIN);
    }

    private void addRole(EnumRole roleName) {
        if (!roleRepository.existsByName(roleName)) {
            Role role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
        }
    }
}
