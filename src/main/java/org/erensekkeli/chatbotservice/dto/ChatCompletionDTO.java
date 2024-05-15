package org.erensekkeli.chatbotservice.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.erensekkeli.chatbotservice.request.ChatMessageRequest;

@Getter
@Setter
@RequiredArgsConstructor
public class ChatCompletionDTO {
    private String model;
    private ChatMessageRequest message;
    private boolean done;
    private String sessionKey;

}
