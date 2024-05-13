package org.erensekkeli.chatbotservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.erensekkeli.chatbotservice.general.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_message")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserMessage extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UserMessage")
    @SequenceGenerator(name = "UserMessage", sequenceName = "USER_MESSAGE_ID_SEQ", allocationSize = 1)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @Column(name = "message_time", nullable = false)
    private LocalDateTime messageTime;

    @Column(name = "is_customer", nullable = false)
    private Boolean isCustomer;

}
