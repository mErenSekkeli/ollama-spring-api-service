package org.erensekkeli.chatbotservice.dto;

import org.erensekkeli.chatbotservice.enums.EnumSessionStatus;

public record SessionUpdateRequest(
        EnumSessionStatus status
) {
}
