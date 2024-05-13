package org.erensekkeli.chatbotservice.controller.contract.impl;

import lombok.RequiredArgsConstructor;
import org.erensekkeli.chatbotservice.controller.contract.SessionControllerContract;
import org.erensekkeli.chatbotservice.dto.SessionDTO;
import org.erensekkeli.chatbotservice.dto.SessionSaveRequest;
import org.erensekkeli.chatbotservice.dto.SessionUpdateRequest;
import org.erensekkeli.chatbotservice.entity.Customer;
import org.erensekkeli.chatbotservice.entity.Session;
import org.erensekkeli.chatbotservice.enums.EnumSessionStatus;
import org.erensekkeli.chatbotservice.exceptions.ItemNotFoundException;
import org.erensekkeli.chatbotservice.mapper.SessionMapper;
import org.erensekkeli.chatbotservice.service.CustomerService;
import org.erensekkeli.chatbotservice.service.SessionService;
import org.erensekkeli.chatbotservice.util.RandomKeyGenerator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessionControllerContractImpl implements SessionControllerContract {

    private final SessionService sessionService;

    private final CustomerService customerService;

    @Override
    public List<SessionDTO> getAllSessions() {
        List<Session> allSessions = sessionService.findAll();

        return SessionMapper.INSTANCE.convertToSessionDTOs(allSessions);
    }

    @Override
    public List<SessionDTO> getAllActiveSessions() {
        List<Session> allActiveSessions = sessionService.findAllActiveSessions();

        return SessionMapper.INSTANCE.convertToSessionDTOs(allActiveSessions);
    }

    @Override
    public SessionDTO getSessionById(Long id) {
        Session session = sessionService.findByIdWithControl(id);

        return SessionMapper.INSTANCE.convertToSessionDTO(session);
    }

    @Override
    public SessionDTO getSessionByKey(String key) {
        Optional<Session> session = sessionService.findByKey(key);

        if (session.isEmpty()) {
            throw new ItemNotFoundException("Session not found with key: " + key);
        }

        session.get().setLastActivityTime(LocalDateTime.now());
        sessionService.save(session.get());
        return SessionMapper.INSTANCE.convertToSessionDTO(session.get());
    }

    @Override
    public SessionDTO saveSession(SessionSaveRequest request) {
        Customer customer = customerService.findByIdWithControl(request.customerId());

        Session newSession = createNewSession(customer);

        return SessionMapper.INSTANCE.convertToSessionDTO(newSession);
    }

    private Session createNewSession(Customer customer) {
        Session newSession = new Session();
        newSession.setCustomer(customer);
        newSession.setStatus(EnumSessionStatus.ACTIVE);
        newSession.setLastActivityTime(LocalDateTime.now());
        newSession.setSessionKey(RandomKeyGenerator.generateComplexId());

        return sessionService.save(newSession);
    }

    @Override
    public SessionDTO updateSession(Long id, SessionUpdateRequest request) {
        Session session = sessionService.findByIdWithControl(id);

        SessionMapper.INSTANCE.updateSessionFields(session, request);

        sessionService.save(session);

        return SessionMapper.INSTANCE.convertToSessionDTO(session);
    }

    @Override
    public void deleteSession(String key) {
        Optional<Session> session = sessionService.findByKey(key);

        if (session.isEmpty()) {
            throw new ItemNotFoundException("Session not found with key: " + key);
        }

        sessionService.delete(session.get());
    }
}
