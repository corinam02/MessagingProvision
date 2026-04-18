package com.example.messageprovisioning.billing.listener;

import com.example.messageprovisioning.billing.dto.BillingNotificationRequest;
import com.example.messageprovisioning.billing.service.BillingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BillingEventListener {
    private static final Logger log =
            LoggerFactory.getLogger(BillingEventListener.class);

    private final BillingService billingService;
    private final ObjectMapper   objectMapper;

    public BillingEventListener(BillingService billingService,
                                ObjectMapper objectMapper) {
        this.billingService = billingService;
        this.objectMapper   = objectMapper;
    }


    @SqsListener("${aws.sqs.billing-queue-url}")
    public void handleBillingEvent(String messageBody) {
        BillingNotificationRequest notification = null;
        try {

            notification = objectMapper.readValue(messageBody, BillingNotificationRequest.class);


            if (notification.getCorrelationId() != null) {
                MDC.put("correlationId", notification.getCorrelationId());
            }
            log.info("Billing event arrived: eventId={}, tip={}, tenant={}",
                    notification.getEventId(), notification.getEventType(),
                    notification.getTenantId());

            billingService.recordCharge(notification);

        } catch (Exception e) {
            log.error("Billign event failed. Body: {}", messageBody, e);
            throw new RuntimeException("Billing event failed", e);
        } finally {
            MDC.remove("correlationId");
        }
    }
}

