package org.erensekkeli.chatbotservice.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.erensekkeli.chatbotservice.enums.EnumStatus;

import java.time.LocalDate;

@Getter
@Setter
public class CustomerUpdateRequest {
    private String name;
    private String surname;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private EnumStatus status;
}
