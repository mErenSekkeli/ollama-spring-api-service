package org.erensekkeli.chatbotservice.service;

import org.erensekkeli.chatbotservice.entity.Session;
import org.erensekkeli.chatbotservice.entity.UserMessage;
import org.erensekkeli.chatbotservice.exceptions.InvalidSessionException;
import org.erensekkeli.chatbotservice.exceptions.ItemNotFoundException;
import org.erensekkeli.chatbotservice.request.ChatContentRequest;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LlamaService {

    private final OllamaChatClient chatClient;
    private final SessionService sessionService;
    private final UserMessageService userMessageService;

    @Autowired
    public LlamaService(OllamaChatClient chatClient, SessionService sessionService, UserMessageService userMessageService) {
        this.chatClient = chatClient;
        this.sessionService = sessionService;
        this.userMessageService = userMessageService;
    }

    public Map<String, String> generateResponse(ChatContentRequest request) {
        if (isSessionKeyValid(request.sessionKey(), request.userId())) {
            throw new InvalidSessionException("Session key is not valid for this user id");
        }
        String message = request.content();

        return Map.of("response", chatClient.call(message));
    }

    public Flux<ChatResponse> generateStreamResponse(ChatContentRequest request) {
        if (isSessionKeyValid(request.sessionKey(), request.userId())) {
            throw new InvalidSessionException("Session key is not valid for this user id");
        }

        // Retrieve the session based on the session key
        Optional<Session> session = sessionService.findByKey(request.sessionKey());

        if (session.isEmpty()) {
            throw new ItemNotFoundException("Session not found");
        }

        // Create a new user message
        UserMessage userMessage = new UserMessage();
        userMessage.setSession(session.get());
        userMessage.setContent(request.content());
        userMessage.setMessageTime(LocalDateTime.now());
        userMessage.setIsCustomer(true);

        // Save the user message
        userMessageService.save(userMessage);

        // Retrieve the chat history for the current session
        List<UserMessage> chatHistory = userMessageService.findBySessionKey(session.get().getSessionKey());

        // Convert the chat history to a list of Ollama Message objects
        String promptString = chatHistory.stream()
                .map(msg -> (msg.getIsCustomer() ? "USER: " : "ASSISTANT: ") + msg.getContent())
                .collect(Collectors.joining("\n"));

        promptString += "\nUSER: " + request.content();

        Prompt prompt = new Prompt(new org.springframework.ai.chat.messages.UserMessage(promptString));
        // Send the chat request to the Ollama API
        return chatClient.stream(prompt)
                .flatMap(response -> {
                    // Create a new user message
                    UserMessage responseMessage = new UserMessage();
                    responseMessage.setSession(session.get());
                    responseMessage.setContent(response.getResult().getOutput().getContent());
                    responseMessage.setMessageTime(LocalDateTime.now());
                    responseMessage.setIsCustomer(false);

                    // Save the user message
                    userMessageService.save(responseMessage);

                    return Flux.just(response);
                });
    }

    private boolean isSessionKeyValid(String sessionKey, Long userId) {
        return !sessionService.isSessionKeyValid(sessionKey, userId);
    }
}
