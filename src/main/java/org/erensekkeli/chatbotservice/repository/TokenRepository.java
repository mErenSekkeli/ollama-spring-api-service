package org.erensekkeli.chatbotservice.repository;

import org.erensekkeli.chatbotservice.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TokenRepository extends JpaRepository<Token, String> {

    List<Token> findByDateBefore(LocalDateTime date);

}