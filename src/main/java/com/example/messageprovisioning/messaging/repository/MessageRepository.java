package com.example.messageprovisioning.messaging.repository;

import com.example.messageprovisioning.messaging.model.Message;
import com.example.messageprovisioning.messaging.model.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,String> {
    List<Message> findByTenantId(String tenantId);
    List<Message> findByFromNumber(String fromNumber);
    List<Message> findByTenantIdAndStatus(String tenantId, MessageStatus status);
}
