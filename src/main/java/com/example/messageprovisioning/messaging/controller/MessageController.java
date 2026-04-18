package com.example.messageprovisioning.messaging.controller;

import com.example.messageprovisioning.messaging.dto.*;
import com.example.messageprovisioning.messaging.service.MessageDispatchService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {

    private final MessageDispatchService dispatchService;

    public MessageController(MessageDispatchService dispatchService) {
        this.dispatchService = dispatchService;
    }


    @PostMapping("/send")
    public ResponseEntity<MessageStatusResponse> send(
            @Valid @RequestBody SendMessageRequest request) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(dispatchService.sendMessage(request));
    }


    @GetMapping("/{messageId}/status")
    public ResponseEntity<MessageStatusResponse> getStatus(
            @PathVariable String messageId) {
        return ResponseEntity.ok(dispatchService.getStatus(messageId));
    }


    @GetMapping("/history")
    public ResponseEntity<List<MessageStatusResponse>> getHistory(
            @RequestParam String tenantId) {
        return ResponseEntity.ok(dispatchService.getHistory(tenantId));
    }
}