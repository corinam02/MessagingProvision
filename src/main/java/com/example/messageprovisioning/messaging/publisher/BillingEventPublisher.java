package com.example.messageprovisioning.messaging.publisher;

import com.example.messageprovisioning.billing.dto.BillingEventType;
import com.example.messageprovisioning.messaging.dto.SendMessageRequest;
import com.example.messageprovisioning.billing.dto.BillingNotificationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

@Component
public class BillingEventPublisher {
    private static final Logger log =
            LoggerFactory.getLogger(BillingEventPublisher.class);

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;

    @Value( "${aws.sqs.billing-queue-url}")
    private String billingQueueUrl;

    private static final BigDecimal SMS_PRICE = new BigDecimal("0.0075");

    public BillingEventPublisher(SqsClient sqsClient, ObjectMapper objectMapper) {
        this.sqsClient    = sqsClient;
        this.objectMapper = objectMapper;
    }

    public void publishSmsSentEvent(String tenantId, String messageId, int count) {
        try {
            BillingNotificationRequest notification = new BillingNotificationRequest(
                    BillingEventType.SMS_SENT, tenantId, messageId,
                    count, SMS_PRICE, MDC.get("correlationId")
            );
            String body = objectMapper.writeValueAsString(notification);


            sqsClient.sendMessage(
                    software.amazon.awssdk.services.sqs.model.SendMessageRequest.builder()
                            .queueUrl(billingQueueUrl)
                            .messageBody(body)
                            .messageDeduplicationId(notification.getEventId())
                            .messageGroupId(tenantId)
                            .build()
            );

            log.info("Billing event: tenant={}, messageId={}, total={}",
                    tenantId, messageId, notification.getTotalAmount());

        } catch (Exception e) {
            log.error("The publishing of the billing event failed: " +
                    "messageId={}, tenant={}", messageId, tenantId, e);
        }
    }
}
