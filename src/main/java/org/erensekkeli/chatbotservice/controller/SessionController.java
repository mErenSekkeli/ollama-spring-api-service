package org.erensekkeli.chatbotservice.controller;

import org.erensekkeli.chatbotservice.controller.contract.SessionControllerContract;
import org.erensekkeli.chatbotservice.dto.SessionDTO;
import org.erensekkeli.chatbotservice.dto.SessionSaveRequest;
import org.erensekkeli.chatbotservice.dto.SessionUpdateRequest;
import org.erensekkeli.chatbotservice.general.RestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/session")
@Validated
public class SessionController {

    private final SessionControllerContract sessionControllerContract;

    public SessionController(SessionControllerContract sessionControllerContract) {
        this.sessionControllerContract = sessionControllerContract;
    }

    //TODO: this endpoint should only be accessible by admin users
    @GetMapping("/all-sessions")
    public ResponseEntity<RestResponse<List<SessionDTO>>> getAllSessions() {
        List<SessionDTO> allSessions = sessionControllerContract.getAllSessions();
        return ResponseEntity.ok(RestResponse.of(allSessions));
    }

    //TODO: this endpoint should only be accessible by admin users
    @GetMapping("/all-active-sessions")
    public ResponseEntity<RestResponse<List<SessionDTO>>> getAllActiveSessions() {
        List<SessionDTO> allActiveSessions = sessionControllerContract.getAllActiveSessions();
        return ResponseEntity.ok(RestResponse.of(allActiveSessions));
    }

    @GetMapping("/{key}")
    public ResponseEntity<RestResponse<SessionDTO>> getSessionByKey(@PathVariable String key) {
        SessionDTO sessionByKey = sessionControllerContract.getSessionByKey(key);
        return ResponseEntity.ok(RestResponse.of(sessionByKey));
    }

    //TODO: this endpoint should only be accessible by admin users
    @GetMapping("/with-id/{id}")
    public ResponseEntity<RestResponse<SessionDTO>> getSessionById(@PathVariable Long id) {
        SessionDTO sessionById = sessionControllerContract.getSessionById(id);
        return ResponseEntity.ok(RestResponse.of(sessionById));
    }

    @PostMapping
    public ResponseEntity<RestResponse<SessionDTO>> saveSession(@RequestBody SessionSaveRequest request) {
        SessionDTO savedSession = sessionControllerContract.saveSession(request);
        return ResponseEntity.ok(RestResponse.of(savedSession));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestResponse<SessionDTO>> updateSession(@PathVariable Long id, @RequestBody SessionUpdateRequest request) {
        SessionDTO updatedSession = sessionControllerContract.updateSession(id, request);
        return ResponseEntity.ok(RestResponse.of(updatedSession));
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<RestResponse<String>> deleteSession(@PathVariable String key) {
        sessionControllerContract.deleteSession(key);
        return ResponseEntity.ok(RestResponse.of("Session deleted successfully"));
    }

}
