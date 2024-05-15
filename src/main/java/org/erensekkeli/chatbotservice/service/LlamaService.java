package org.erensekkeli.chatbotservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.erensekkeli.chatbotservice.dto.ChatCompletionDTO;
import org.erensekkeli.chatbotservice.entity.Customer;
import org.erensekkeli.chatbotservice.entity.Session;
import org.erensekkeli.chatbotservice.entity.UserMessage;
import org.erensekkeli.chatbotservice.enums.EnumSessionStatus;
import org.erensekkeli.chatbotservice.exceptions.InvalidSessionException;
import org.erensekkeli.chatbotservice.exceptions.ItemNotFoundException;
import org.erensekkeli.chatbotservice.request.ChatCompletionRequest;
import org.erensekkeli.chatbotservice.request.ChatContentRequest;
import org.erensekkeli.chatbotservice.request.ChatMessageRequest;
import org.erensekkeli.chatbotservice.request.SessionSaveRequest;
import org.erensekkeli.chatbotservice.util.RandomKeyGenerator;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LlamaService {

    private final OllamaChatClient chatClient;
    private final SessionService sessionService;
    private final CustomerService customerService;
    private final UserMessageService userMessageService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.ai.ollama.base-url}")
    private String baseUrl;

    @Autowired
    public LlamaService(OllamaChatClient chatClient, SessionService sessionService, CustomerService customerService, UserMessageService userMessageService, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.chatClient = chatClient;
        this.sessionService = sessionService;
        this.customerService = customerService;
        this.userMessageService = userMessageService;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public Map<String, String> generateResponse(ChatContentRequest request) {
        if (isSessionKeyValid(request.sessionKey(), request.userId())) {
            throw new InvalidSessionException("Session key is not valid for this user id");
        }
        String message = request.content();

        return Map.of("response", chatClient.call(message));
    }


    public ChatCompletionDTO generateStreamResponse(ChatContentRequest request) {
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
        List<ChatMessageRequest> messages = chatHistory.stream()
                .map(msg -> new ChatMessageRequest(
                        msg.getIsCustomer() ? "user" : "assistant",
                        msg.getContent()))
                .collect(Collectors.toList());

        // Add the current user message to the list of messages
        messages.add(new ChatMessageRequest("user", request.content()));

        // Create a ChatCompletionRequest object
        ChatCompletionRequest chatCompletionRequest = new ChatCompletionRequest();
        chatCompletionRequest.setModel("llama3");
        chatCompletionRequest.setMessages(messages);

        // Send the chat request to the Ollama API
        ChatCompletionDTO response = generateChatCompletion(messages);

        // Create a new user message for the assistant's response
        UserMessage responseMessage = new UserMessage();
        responseMessage.setSession(session.get());
        responseMessage.setContent(response.getMessage().getContent());
        responseMessage.setMessageTime(LocalDateTime.now());
        responseMessage.setIsCustomer(false);

        // Save the user message
        userMessageService.save(responseMessage);

        return response;
    }
    //TODO: stream false is ending the chat so you can start a new chat. Past messages are already saved in the database.
    public ChatCompletionDTO startNewChat(SessionSaveRequest request) {
        Customer customer = customerService.findByIdWithControl(request.customerId());

        // Create a new session for the customer
        Session session = new Session();
        session.setCustomer(customer);
        session.setSessionKey(RandomKeyGenerator.generateComplexId());
        session.setLastActivityTime(LocalDateTime.now());
        session.setStatus(EnumSessionStatus.ACTIVE);

        // Save the session
        sessionService.save(session);

        // Create a ChatCompletionRequest object
        ChatCompletionRequest chatCompletionRequest = new ChatCompletionRequest();
        chatCompletionRequest.setModel(request.model());
        chatCompletionRequest.setMessages(Collections.emptyList());

        // Send the chat request to the Ollama API
        ChatCompletionDTO response = generateChatCompletion(chatCompletionRequest.getMessages());
        response.setSessionKey(session.getSessionKey());
        // Create a new user message for the assistant's response
        UserMessage responseMessage = new UserMessage();
        responseMessage.setSession(session);
        responseMessage.setContent(response.getMessage().getContent());
        responseMessage.setMessageTime(LocalDateTime.now());
        responseMessage.setIsCustomer(false);

        // Save the user message
        userMessageService.save(responseMessage);

        return response;
    }

    private ChatCompletionDTO generateChatCompletion(List<ChatMessageRequest> messages) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ChatCompletionRequest request = new ChatCompletionRequest();
        request.setModel("llama3");
        request.setMessages(messages);

        HttpEntity<ChatCompletionRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/api/chat",
                HttpMethod.POST,
                entity,
                String.class
        );

        String responseBody = response.getBody();
        try {
            StringBuilder contentBuilder = new StringBuilder();
            String[] jsonObjects = responseBody.split("\\r?\\n");
            ChatCompletionDTO chatCompletion = null;

            for (String jsonObject : jsonObjects) {
                if (!jsonObject.trim().isEmpty()) {
                    JsonNode jsonNode = objectMapper.readTree(jsonObject);
                    ChatCompletionDTO partialCompletion = objectMapper.treeToValue(jsonNode, ChatCompletionDTO.class);

                    if (chatCompletion == null) {
                        chatCompletion = partialCompletion;
                    }

                    contentBuilder.append(partialCompletion.getMessage().getContent());

                    if (partialCompletion.isDone()) {
                        break;
                    }
                }
            }

            if (chatCompletion != null) {
                chatCompletion.getMessage().setContent(contentBuilder.toString());
            }

            return chatCompletion;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error while parsing JSON response", e);
        }
    }


    private boolean isSessionKeyValid(String sessionKey, Long userId) {
        return !sessionService.isSessionKeyValid(sessionKey, userId);
    }
}
