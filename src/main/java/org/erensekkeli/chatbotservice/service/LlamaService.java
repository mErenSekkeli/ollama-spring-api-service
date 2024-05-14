package org.erensekkeli.chatbotservice.service;

import org.erensekkeli.chatbotservice.dto.ChatResponseDTO;
import org.erensekkeli.chatbotservice.entity.Session;
import org.erensekkeli.chatbotservice.entity.UserMessage;
import org.erensekkeli.chatbotservice.exceptions.InvalidSessionException;
import org.erensekkeli.chatbotservice.exceptions.ItemNotFoundException;
import org.erensekkeli.chatbotservice.request.ChatContentRequest;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.ChatMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

    @Value("${spring.ai.ollama.base-url}")
    private String baseUrl;

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

    public ChatResponseDTO generateStreamResponse(ChatContentRequest request) {
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

        // Convert the chat history to a list of messages for the Ollama API
        List<OllamaApi.Message> messages = chatHistory.stream()
                .map(msg -> new OllamaApi.Message(
                        msg.getIsCustomer() ? OllamaApi.Message.Role.USER : OllamaApi.Message.Role.ASSISTANT,
                        msg.getContent(),
                        null))
                .collect(Collectors.toList());

        // Add the current user message to the list of messages
        messages.add(new OllamaApi.Message(OllamaApi.Message.Role.USER, request.content(), null));

        // Create a ChatRequest object
        OllamaApi.ChatRequest chatRequest = new OllamaApi.ChatRequest(
                request.model(),
                messages,
                true,
                "json",
                null
        );

        // Create a WebClient instance
        WebClient webClient = WebClient.create(baseUrl);

        // Send the chat request to the Ollama API
        ChatResponseDTO response = webClient.post()
                .uri("/api/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(chatRequest)
                .retrieve()
                .bodyToMono(ChatResponseDTO.class)
                .block();

        // Create a new user message for the assistant's response
        UserMessage responseMessage = new UserMessage();
        responseMessage.setSession(session.get());
        assert response != null;
        responseMessage.setContent(response.getMessage().getContent());
        responseMessage.setMessageTime(LocalDateTime.now());
        responseMessage.setIsCustomer(false);

        // Save the user message
        userMessageService.save(responseMessage);

        return response;
    }

    private boolean isSessionKeyValid(String sessionKey, Long userId) {
        return !sessionService.isSessionKeyValid(sessionKey, userId);
    }
}
