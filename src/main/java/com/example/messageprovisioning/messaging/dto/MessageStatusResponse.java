package com.example.messageprovisioning.messaging.dto;

import com.example.messageprovisioning.messaging.model.Message;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class MessageStatusResponse {
    private String messageId;
    private String status;
    private String fromNumber;
    private String toNumber;
    private LocalDateTime queuedAt;
    private LocalDateTime sentAt;
    private LocalDateTime deliveredAt;
    private String failureReason;
    private String message;

    public MessageStatusResponse(Message entity) {
        this.messageId = entity.getId();
        this.status = entity.getStatus().name();
        this.fromNumber = entity.getFromNumber();
        this.toNumber = entity.getToNumber();
        this.queuedAt = entity.getQueuedAt();
        this.sentAt = entity.getSentAt();
        this.deliveredAt = entity.getDeliveredAt();
        this.failureReason = entity.getFailureReason();
        this.message = "SMS " + entity.getStatus().name().toLowerCase()
                + " from " + entity.getFromNumber();
    }
}