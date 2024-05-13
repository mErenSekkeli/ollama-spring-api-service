package org.erensekkeli.chatbotservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.erensekkeli.chatbotservice.enums.EnumSessionStatus;
import org.erensekkeli.chatbotservice.general.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "session")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Session extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Session")
    @SequenceGenerator(name = "Session", sequenceName = "SESSION_ID_SEQ", allocationSize = 1)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "session_key", nullable = false, unique = true)
    private String sessionKey;

    @Column(name = "last_activity_time", nullable = false)
    private LocalDateTime lastActivityTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private EnumSessionStatus status;

}
