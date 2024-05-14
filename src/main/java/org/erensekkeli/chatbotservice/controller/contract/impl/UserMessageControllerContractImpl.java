package org.erensekkeli.chatbotservice.controller.contract.impl;

import lombok.RequiredArgsConstructor;
import org.erensekkeli.chatbotservice.controller.contract.UserMessageControllerContract;
import org.erensekkeli.chatbotservice.dto.UserMessageDTO;
import org.erensekkeli.chatbotservice.request.UserMessageSendRequest;
import org.erensekkeli.chatbotservice.entity.Session;
import org.erensekkeli.chatbotservice.entity.UserMessage;
import org.erensekkeli.chatbotservice.exceptions.ItemNotFoundException;
import org.erensekkeli.chatbotservice.mapper.UserMessageMapper;
import org.erensekkeli.chatbotservice.service.SessionService;
import org.erensekkeli.chatbotservice.service.UserMessageService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserMessageControllerContractImpl implements UserMessageControllerContract {

    private final UserMessageService userMessageService;

    private final SessionService sessionService;

    @Override
    public List<UserMessageDTO> getAllUserMessages() {
        List<UserMessage> allUserMessages = userMessageService.findAll();

        return UserMessageMapper.INSTANCE.convertToUserMessageDTOs(allUserMessages);
    }

    @Override
    public UserMessageDTO getUserMessageById(Long id) {
        UserMessage userMessage = userMessageService.findByIdWithControl(id);

        return UserMessageMapper.INSTANCE.convertToUserMessageDTO(userMessage);
    }

    @Override
    public List<UserMessageDTO> getUserMessagesBySessionId(Long sessionId) {
        List<UserMessage> userMessagesBySessionId = userMessageService.findBySessionId(sessionId);

        return UserMessageMapper.INSTANCE.convertToUserMessageDTOs(userMessagesBySessionId);
    }

    @Override
    public List<UserMessageDTO> getUserMessagesBySessionKey(String sessionKey) {
        List<UserMessage> userMessagesBySessionKey = userMessageService.findBySessionKey(sessionKey);

        return UserMessageMapper.INSTANCE.convertToUserMessageDTOs(userMessagesBySessionKey);
    }

    @Override
    public UserMessageDTO getLatestUserMessageBySessionKey(String sessionKey) {
        UserMessage latestUserMessageBySessionKey = userMessageService.findLatestBySessionKey(sessionKey);

        return UserMessageMapper.INSTANCE.convertToUserMessageDTO(latestUserMessageBySessionKey);
    }

    @Override
    public UserMessageDTO getLatestUserMessagesBySessionId(Long sessionId) {
        UserMessage latestUserMessagesBySessionId = userMessageService.findLatestBySessionId(sessionId);

        return UserMessageMapper.INSTANCE.convertToUserMessageDTO(latestUserMessagesBySessionId);
    }

    @Override
    public UserMessageDTO sendUserMessage(UserMessageSendRequest request) {
        Optional<Session> session = sessionService.findByKey(request.sessionKey());
        if (session.isEmpty()) {
            throw new ItemNotFoundException("Session not found with key: " + request.sessionKey());
        }
        //TODO: check if the message is belong to the session owner
        UserMessage userMessage = createNewUserMessage(request, session.get());

        return UserMessageMapper.INSTANCE.convertToUserMessageDTO(userMessage);
    }

    @Override
    public void deleteUserMessage(Long id) {
        Optional<UserMessage> userMessage = userMessageService.findById(id);
        if (userMessage.isEmpty()) {
            throw new ItemNotFoundException("User message not found with id: " + id);
        }
        userMessageService.delete(id);
    }

    private UserMessage createNewUserMessage(UserMessageSendRequest request, Session session) {
        UserMessage userMessage = new UserMessage();
        userMessage.setSession(session);
        userMessage.setContent(request.content());
        userMessage.setMessageTime(LocalDateTime.now());
        userMessage.setIsCustomer(true);
        return userMessageService.save(userMessage);
    }

}
