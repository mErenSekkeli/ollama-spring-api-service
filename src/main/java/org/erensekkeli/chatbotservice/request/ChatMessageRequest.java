package org.erensekkeli.chatbotservice.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ChatMessageRequest {
    private String role;
    private String content;

}