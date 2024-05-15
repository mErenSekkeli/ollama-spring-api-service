package org.erensekkeli.chatbotservice.controller;

import lombok.RequiredArgsConstructor;
import org.erensekkeli.chatbotservice.dto.ChatCompletionDTO;
import org.erensekkeli.chatbotservice.dto.ChatResponseDTO;
import org.erensekkeli.chatbotservice.general.RestResponse;
import org.erensekkeli.chatbotservice.request.ChatContentRequest;
import org.erensekkeli.chatbotservice.request.SessionSaveRequest;
import org.erensekkeli.chatbotservice.service.LlamaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final LlamaService llamaService;

    @PostMapping("/message")
    public ResponseEntity<RestResponse<ChatResponseDTO>> processUserMessage(@RequestBody ChatContentRequest request) {
        Map<String, String> llamaResponse = llamaService.generateResponse(request);
        ChatResponseDTO chatResponseDTO = new ChatResponseDTO();
        chatResponseDTO.setMessage(new ChatResponseDTO.Message());
        chatResponseDTO.getMessage().setContent(llamaResponse.get("response"));
        return ResponseEntity.ok(RestResponse.of(chatResponseDTO));
    }

    @PostMapping("/message/start")
    public ResponseEntity<RestResponse<ChatCompletionDTO>> processUserMessageStart(@RequestBody SessionSaveRequest request) {
        ChatCompletionDTO llamaStartResponse = llamaService.startNewChat(request);
        return ResponseEntity.ok(RestResponse.of(llamaStartResponse));
    }


    @PostMapping("/message/stream")
    public ResponseEntity<RestResponse<ChatCompletionDTO>> processUserMessageStream(@RequestBody ChatContentRequest request) {
        ChatCompletionDTO llamaStreamResponse = llamaService.generateStreamResponse(request);
        return ResponseEntity.ok(RestResponse.of(llamaStreamResponse));
    }
}
