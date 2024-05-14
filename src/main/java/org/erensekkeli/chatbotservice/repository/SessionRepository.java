package org.erensekkeli.chatbotservice.repository;

import org.erensekkeli.chatbotservice.entity.Session;
import org.erensekkeli.chatbotservice.enums.EnumSessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {
    

    List<Session> findAllByStatus(EnumSessionStatus enumSessionStatus);

    Optional<Session> findBySessionKey(String key);

    Optional<Object> findBySessionKeyAndCustomerId(String s, Long aLong);
}
