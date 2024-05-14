package org.erensekkeli.chatbotservice.controller.contract;

import org.erensekkeli.chatbotservice.dto.SessionDTO;
import org.erensekkeli.chatbotservice.request.SessionSaveRequest;
import org.erensekkeli.chatbotservice.request.SessionUpdateRequest;

import java.util.List;

public interface SessionControllerContract {
    SessionDTO getSessionByKey(String key);

    List<SessionDTO> getAllSessions();

    List<SessionDTO> getAllActiveSessions();

    SessionDTO getSessionById(Long id);

    SessionDTO saveSession(SessionSaveRequest request);

    SessionDTO updateSession(Long id, SessionUpdateRequest request);

    void deleteSession(String key);
}
