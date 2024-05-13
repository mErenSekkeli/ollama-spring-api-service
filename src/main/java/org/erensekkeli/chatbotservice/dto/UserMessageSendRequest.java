package org.erensekkeli.chatbotservice.dto;

public record UserMessageSendRequest(
        String sessionKey,
        String content
) {
}
