package org.erensekkeli.chatbotservice.dto;

import lombok.Data;

@Data
public class AuthResponse {

    private String token;

    private CustomerDTO customer;
}
