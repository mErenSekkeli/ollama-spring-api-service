package org.erensekkeli.chatbotservice.dto;

import org.erensekkeli.chatbotservice.enums.EnumStatus;

import java.time.LocalDate;

public record CustomerDTO(Long id,
                          String name,
                          String surname,
                          String username,
                          String email,
                          LocalDate birthDate,
                          EnumStatus status) {
}
