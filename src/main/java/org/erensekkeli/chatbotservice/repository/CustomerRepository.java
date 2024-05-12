package org.erensekkeli.chatbotservice.repository;

import org.erensekkeli.chatbotservice.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
