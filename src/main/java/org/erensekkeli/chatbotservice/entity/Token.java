package org.erensekkeli.chatbotservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens")
@Data
public class Token {

    @Id
    private String token;
    @ManyToOne
    private Customer customer;
    @CreationTimestamp
    private LocalDateTime date;
}
