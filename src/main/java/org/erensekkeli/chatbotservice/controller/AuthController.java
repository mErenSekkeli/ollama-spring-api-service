package org.erensekkeli.chatbotservice.controller;

import lombok.AllArgsConstructor;
import org.erensekkeli.chatbotservice.dto.AuthResponse;
import org.erensekkeli.chatbotservice.general.RestResponse;
import org.erensekkeli.chatbotservice.request.LoginRequest;
import org.erensekkeli.chatbotservice.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public AuthResponse handleAuth(@RequestBody LoginRequest request){
        return authService.authenticate(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<RestResponse<Boolean>> handleLogout(@RequestHeader String Authorization){
        String token = Authorization.substring(7);
        authService.clearToken(token);
        return ResponseEntity.ok(RestResponse.of(true));
    }
}
