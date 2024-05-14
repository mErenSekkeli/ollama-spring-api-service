package org.erensekkeli.chatbotservice.controller.contract;

import org.erensekkeli.chatbotservice.dto.UserMessageDTO;
import org.erensekkeli.chatbotservice.request.UserMessageSendRequest;

import java.util.List;

public interface UserMessageControllerContract {
    List<UserMessageDTO> getAllUserMessages();

    UserMessageDTO getUserMessageById(Long id);

    List<UserMessageDTO> getUserMessagesBySessionId(Long sessionId);

    List<UserMessageDTO> getUserMessagesBySessionKey(String sessionKey);

    UserMessageDTO getLatestUserMessageBySessionKey(String sessionKey);

    UserMessageDTO getLatestUserMessagesBySessionId(Long sessionId);

    UserMessageDTO sendUserMessage(UserMessageSendRequest request);

    void deleteUserMessage(Long id);
}
