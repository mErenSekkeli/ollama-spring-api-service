package org.erensekkeli.chatbotservice.repository;

import org.erensekkeli.chatbotservice.entity.UserMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserMessageRepository extends JpaRepository<UserMessage, Long> {
    List<UserMessage> findBySessionId(Long sessionId);

    @Query(value = "SELECT um.* FROM user_message um" +
            " INNER JOIN session s ON um.session_id = s.id" +
            " WHERE s.session_key = :sessionKey", nativeQuery = true)
    List<UserMessage> findBySessionKey(String sessionKey);

    @Query(value = "SELECT um.* FROM user_message um " +
            "INNER JOIN session s ON um.session_id = s.id " +
            "WHERE s.session_key = :sessionKey " +
            "ORDER BY um.message_time DESC LIMIT 1", nativeQuery = true)
    UserMessage findLatestBySessionKey(String sessionKey);

    UserMessage findLatestBySessionId(Long sessionId);
}
