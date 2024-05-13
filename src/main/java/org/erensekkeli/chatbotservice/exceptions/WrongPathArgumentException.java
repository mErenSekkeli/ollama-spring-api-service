package org.erensekkeli.chatbotservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WrongPathArgumentException extends RuntimeException{
    public WrongPathArgumentException(String message) {
        super(message);
    }
}
