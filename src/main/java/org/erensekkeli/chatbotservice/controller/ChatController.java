package org.erensekkeli.chatbotservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.erensekkeli.chatbotservice.dto.ChatCompletionDTO;
import org.erensekkeli.chatbotservice.dto.ChatResponseDTO;
import org.erensekkeli.chatbotservice.general.RestResponse;
import org.erensekkeli.chatbotservice.request.ChatContentRequest;
import org.erensekkeli.chatbotservice.request.SessionSaveRequest;
import org.erensekkeli.chatbotservice.service.LlamaService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@Validated
public class ChatController {

    private final LlamaService llamaService;

    @PostMapping("/message")
    @Operation(summary = "Process a user message",
            description = "Process a user message with the given user id and session key." +
                    " This endpoint is used for a single message processing." +
                    " If you want to process multiple messages in a chat session, use /message/stream endpoint.")
    public ResponseEntity<RestResponse<ChatResponseDTO>> processUserMessage(@RequestBody ChatContentRequest request) {
        Map<String, String> llamaResponse = llamaService.generateResponse(request);
        ChatResponseDTO chatResponseDTO = new ChatResponseDTO();
        chatResponseDTO.setMessage(new ChatResponseDTO.Message());
        chatResponseDTO.getMessage().setContent(llamaResponse.get("response"));
        return ResponseEntity.ok(RestResponse.of(chatResponseDTO));
    }

    @PostMapping("/message/start")
    @Operation(summary = "Start a new chat session",
            description = "Start a new chat session with the given user id." +
                    " You don't need to provide a session key because it will be generated a new session automatically.")
    public ResponseEntity<RestResponse<ChatCompletionDTO>> processUserMessageStart(@RequestBody SessionSaveRequest request) {
        ChatCompletionDTO llamaStartResponse = llamaService.startNewChat(request);
        return ResponseEntity.ok(RestResponse.of(llamaStartResponse));
    }


    @PostMapping("/message/stream")
    @Operation(summary = "Process a user message in a chat session",
            description = " Process a user message in a chat session with the given session key and user id.")
    public ResponseEntity<RestResponse<ChatCompletionDTO>> processUserMessageStream(@RequestBody ChatContentRequest request) {
        ChatCompletionDTO llamaStreamResponse = llamaService.generateStreamResponse(request);
        return ResponseEntity.ok(RestResponse.of(llamaStreamResponse));
    }

    @DeleteMapping("/message/end")
    @Operation(summary = "End a chat session",
            description = " End a chat session with the given session key and user id." +
                    " This endpoint is ending a chat session from llama model and deletes session with user messages.")
    public ResponseEntity<RestResponse<String>> endChat(@RequestBody ChatContentRequest request) {
        return llamaService.endChat(request) ?
                ResponseEntity.ok(RestResponse.of("Chat ended")) :
                ResponseEntity.badRequest().body(RestResponse.of("Chat could not be ended"));
    }
}
