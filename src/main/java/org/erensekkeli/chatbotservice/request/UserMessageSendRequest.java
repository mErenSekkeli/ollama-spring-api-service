package org.erensekkeli.chatbotservice.request;

public record UserMessageSendRequest(
        String sessionKey,
        String content
) {
}
