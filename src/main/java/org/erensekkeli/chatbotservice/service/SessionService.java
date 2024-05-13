package org.erensekkeli.chatbotservice.service;


import org.erensekkeli.chatbotservice.entity.Session;
import org.erensekkeli.chatbotservice.enums.EnumSessionStatus;
import org.erensekkeli.chatbotservice.general.BaseEntityService;
import org.erensekkeli.chatbotservice.repository.SessionRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SessionService extends BaseEntityService<Session, SessionRepository> {
    protected SessionService(SessionRepository repository) {
        super(repository);
    }

    public List<Session> findAllActiveSessions() {
        return getRepository().findAllByStatus(EnumSessionStatus.ACTIVE);
    }

    public Optional<Session> findByKey(String key) {
        return getRepository().findBySessionKey(key);
    }

}
