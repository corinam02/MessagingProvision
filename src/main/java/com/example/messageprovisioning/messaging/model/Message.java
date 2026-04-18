package com.example.messageprovisioning.messaging.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table( name = "messages", indexes = {
        @Index(name = "idx_msg_client", columnList = "clientId")
})
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy =  GenerationType.UUID)
    private String id;

    @Column(name = "from_number", nullable = false, length = 20)
    private String fromNumber;

    @Column(name = "to_number", nullable = false, length = 20)
    private String toNumber;

    @Column(name = "body", nullable = false, length = 1600)
    private String body;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MessageStatus status;

    @Column(name = "tenant_id", nullable = false, length = 100)
    private String tenantId;
    @Column(name = "queued_at")
    private LocalDateTime queuedAt = LocalDateTime.now();

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @Column(name = "failure_reason", length = 255)
    private String failureReason;

}
