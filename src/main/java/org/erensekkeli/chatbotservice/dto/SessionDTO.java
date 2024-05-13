package org.erensekkeli.chatbotservice.dto;

import org.erensekkeli.chatbotservice.enums.EnumSessionStatus;

import java.time.LocalDateTime;

public record SessionDTO(
        Long id,
        Long customerId,
        String sessionKey,
        LocalDateTime createDate,
        LocalDateTime lastActivityTime,
        LocalDateTime endTime,
        EnumSessionStatus status
){
}
