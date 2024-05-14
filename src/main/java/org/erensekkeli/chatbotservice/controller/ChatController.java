package org.erensekkeli.chatbotservice.controller;

import lombok.RequiredArgsConstructor;
import org.erensekkeli.chatbotservice.dto.ChatResponseDTO;
import org.erensekkeli.chatbotservice.general.RestResponse;
import org.erensekkeli.chatbotservice.request.ChatContentRequest;
import org.erensekkeli.chatbotservice.service.LlamaService;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final LlamaService llamaService;

    @PostMapping("/message")
    public ResponseEntity<RestResponse<ChatResponseDTO>> processUserMessage(@RequestBody ChatContentRequest request) {
        Map<String, String> llamaResponse = llamaService.generateResponse(request);
        ChatResponseDTO chatResponseDTO = new ChatResponseDTO(llamaResponse.get("response"));
        return ResponseEntity.ok(RestResponse.of(chatResponseDTO));
    }

    @PostMapping("/message/stream")
    public ResponseEntity<RestResponse<Flux<ChatResponse>>> processUserMessageStream(@RequestBody ChatContentRequest request) {
        Flux<ChatResponse> llamaStreamResponse = llamaService.generateStreamResponse(request);
        return ResponseEntity.ok(RestResponse.of(llamaStreamResponse));
    }
}
