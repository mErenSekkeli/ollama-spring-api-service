package org.erensekkeli.chatbotservice.request;

public record ChatContentRequest(
        Long userId,
        String sessionKey,
        String content,
        String model
) {
}
