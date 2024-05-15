package org.erensekkeli.chatbotservice.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class ChatCompletionRequest {
    private String model;
    private List<ChatMessageRequest> messages;
    private boolean stream;
}
