package org.erensekkeli.chatbotservice.request;

import org.erensekkeli.chatbotservice.enums.EnumSessionStatus;

public record SessionUpdateRequest(
        EnumSessionStatus status
) {
}
