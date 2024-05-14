package org.erensekkeli.chatbotservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatResponseDTO {
    private String model;
    private String createdAt;
    private Message message;
    private boolean done;

    // Getters and setters
    @Getter
    @Setter
    public static class Message {
        private String role;
        private String content;

    }
}
