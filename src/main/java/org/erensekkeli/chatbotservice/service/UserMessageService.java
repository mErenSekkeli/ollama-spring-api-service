package org.erensekkeli.chatbotservice.service;

import org.erensekkeli.chatbotservice.entity.UserMessage;
import org.erensekkeli.chatbotservice.general.BaseEntityService;
import org.erensekkeli.chatbotservice.repository.UserMessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserMessageService extends BaseEntityService<UserMessage, UserMessageRepository> {

    protected UserMessageService(UserMessageRepository repository) {
        super(repository);
    }

    public List<UserMessage> findBySessionId(Long sessionId) {
        return getRepository().findBySessionId(sessionId);
    }

    public List<UserMessage> findBySessionKey(String sessionKey) {
        return getRepository().findBySessionKey(sessionKey);
    }

    public UserMessage findLatestBySessionKey(String sessionKey) {
        return getRepository().findLatestBySessionKey(sessionKey);
    }

    public UserMessage findLatestBySessionId(Long sessionId) {
        return getRepository().findLatestBySessionId(sessionId);
    }
}
