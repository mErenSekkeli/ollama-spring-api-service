package org.erensekkeli.chatbotservice.request;

import lombok.Data;

@Data
public class LoginRequest {

    private String username;
    private String password;
}
