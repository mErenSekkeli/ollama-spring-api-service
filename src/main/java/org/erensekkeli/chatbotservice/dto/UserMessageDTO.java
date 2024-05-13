package org.erensekkeli.chatbotservice.dto;

public record UserMessageDTO(
        Long id,
        Long sessionId,
        String content,
        String messageTime,
        Boolean isCustomer
) {
}
