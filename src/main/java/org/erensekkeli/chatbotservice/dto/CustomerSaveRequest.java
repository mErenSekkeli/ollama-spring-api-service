package org.erensekkeli.chatbotservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.erensekkeli.chatbotservice.enums.EnumStatus;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

public record CustomerSaveRequest(
        @NotBlank @Length(min = 1, max = 100) String name,
        @NotBlank @Length(min = 1, max = 100) String surname,
        @NotNull @Past LocalDate birthDate,
        @NotNull String username,
        @NotBlank String password,
        @NotBlank @Email String email,
        @NotNull EnumStatus status
) {
}
