package com.example.messageprovisioning.messaging.service;


import com.example.messageprovisioning.messaging.dto.MessageStatusResponse;
import com.example.messageprovisioning.messaging.dto.SendMessageRequest;
import com.example.messageprovisioning.messaging.model.Message;
import com.example.messageprovisioning.messaging.model.MessageStatus;
import com.example.messageprovisioning.messaging.publisher.BillingEventPublisher;
import com.example.messageprovisioning.messaging.repository.MessageRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
    public class MessageDispatchService {

        private static final Logger log =
                LoggerFactory.getLogger(MessageDispatchService.class);

        private final MessageRepository messageRepo;
        private final BillingEventPublisher billingPublisher;

        public MessageDispatchService(MessageRepository messageRepo,
                                      BillingEventPublisher billingPublisher) {
            this.messageRepo      = messageRepo;
            this.billingPublisher = billingPublisher;
        }

        @Transactional
        public MessageStatusResponse sendMessage(SendMessageRequest request) {
            log.info("Cerere trimitere SMS: from={}, to={}, tenant={}",
                    request.getFromNumber(), request.getToNumber(), request.getTenantId());

            // Pasul 1: Salveaza mesajul cu status QUEUED
            Message message = new Message();
            message.setFromNumber(request.getFromNumber());
            message.setToNumber(request.getToNumber());
            message.setBody(request.getBody());
            message.setTenantId(request.getTenantId());
            message.setStatus(MessageStatus.QUEUED);
            message = messageRepo.save(message);

            boolean dispatched = dispatchToCarrier(message);

            if (dispatched) {
                message.setStatus(MessageStatus.SENT);
                message.setSentAt(LocalDateTime.now());
                log.info("SMS send succesuflly: messageId={}", message.getId());
            } else {
                message.setStatus(MessageStatus.FAILED);
                message.setFailureReason("Message rejected");
                log.warn("Failed sms: messageId={}", message.getId());
            }
            messageRepo.save(message);


            billingPublisher.publishSmsSentEvent(
                    request.getTenantId(), message.getId(), 1);

            return new MessageStatusResponse(message);
        }

        public MessageStatusResponse getStatus(String messageId) {
            Message message = messageRepo.findById(messageId)
                    .orElseThrow(() -> new RuntimeException("Message not found: " + messageId));
            return new MessageStatusResponse(message);
        }

        public List<MessageStatusResponse> getHistory(String tenantId) {
            return messageRepo.findByTenantId(tenantId)
                    .stream()
                    .map(MessageStatusResponse::new)
                    .collect(Collectors.toList());
        }

        private boolean dispatchToCarrier(Message message) {
            log.info("Send to the operator: messageId={}", message.getId());
            return Math.random() > 0.05;
        }
}
