package org.erensekkeli.chatbotservice.repository;

import org.erensekkeli.chatbotservice.entity.Role;
import org.erensekkeli.chatbotservice.enums.EnumRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(EnumRole name);

    boolean existsByName(EnumRole roleName);
}
