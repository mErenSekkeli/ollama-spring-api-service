package org.erensekkeli.chatbotservice.controller;

import lombok.RequiredArgsConstructor;
import org.erensekkeli.chatbotservice.controller.contract.UserMessageControllerContract;
import org.erensekkeli.chatbotservice.dto.UserMessageDTO;
import org.erensekkeli.chatbotservice.request.UserMessageSendRequest;
import org.erensekkeli.chatbotservice.exceptions.WrongPathArgumentException;
import org.erensekkeli.chatbotservice.general.RestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user-messages")
@Validated
@RequiredArgsConstructor
public class UserMessageController {

    private final UserMessageControllerContract userMessageControllerContract;

    //TODO: this endpoint should only be accessible by admin users
    @GetMapping
    public ResponseEntity<RestResponse<List<UserMessageDTO>>> getAllUserMessages() {
        List<UserMessageDTO> allUserMessages = userMessageControllerContract.getAllUserMessages();
        return ResponseEntity.ok(RestResponse.of(allUserMessages));
    }

    //TODO: this endpoint should only be accessible by admin users
    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<UserMessageDTO>> getUserMessageById(@PathVariable Long id) {
        UserMessageDTO userMessageById = userMessageControllerContract.getUserMessageById(id);
        return ResponseEntity.ok(RestResponse.of(userMessageById));
    }

    @GetMapping("/with-session-id/{sessionId}")
    public ResponseEntity<RestResponse<List<UserMessageDTO>>> getUserMessagesBySessionId(@PathVariable Long sessionId) {
        List<UserMessageDTO> userMessagesBySessionId = userMessageControllerContract.getUserMessagesBySessionId(sessionId);
        return ResponseEntity.ok(RestResponse.of(userMessagesBySessionId));
    }

    @GetMapping("/with-session-key/{sessionKey}")
    public ResponseEntity<RestResponse<List<UserMessageDTO>>> getUserMessagesBySessionKey(@PathVariable String sessionKey) {
        List<UserMessageDTO> userMessagesBySessionKey = userMessageControllerContract.getUserMessagesBySessionKey(sessionKey);
        return ResponseEntity.ok(RestResponse.of(userMessagesBySessionKey));
    }

    @GetMapping({"/with-session-key/{sessionKey}/latest", "/with-session-id/{sessionId}/latest"})
    public ResponseEntity<RestResponse<UserMessageDTO>> getLatestUserMessageBySession(
            @PathVariable(required = false) String sessionKey,
            @PathVariable(required = false) Long sessionId) {
        if (sessionKey == null && sessionId == null) {
            throw new WrongPathArgumentException("Either sessionKey or sessionId must be provided");
        }

        if (sessionKey != null) {
            var latestUserMessageBySessionKey = userMessageControllerContract.getLatestUserMessageBySessionKey(sessionKey);
            return ResponseEntity.ok(RestResponse.of(latestUserMessageBySessionKey));
        } else {
            var userMessagesBySessionId = userMessageControllerContract.getLatestUserMessagesBySessionId(sessionId);
            return ResponseEntity.ok(RestResponse.of(userMessagesBySessionId));
        }

    }

    //TODO: this endpoint should only be accessible by admin users because we already saving the messages on llama service
    @PostMapping("/send")
    public ResponseEntity<RestResponse<UserMessageDTO>> sendUserMessage(@RequestBody UserMessageSendRequest request) {
        UserMessageDTO sentUserMessage = userMessageControllerContract.sendUserMessage(request);
        return ResponseEntity.ok(RestResponse.of(sentUserMessage));
    }

    //TODO: we may also add a new endpoint to update user messages

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<String>> deleteUserMessage(@PathVariable Long id) {
        userMessageControllerContract.deleteUserMessage(id);
        return ResponseEntity.ok(RestResponse.of("User message deleted successfully"));
    }

}
