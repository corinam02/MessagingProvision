package com.example.messageprovisioning.billing.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter @Setter
@NoArgsConstructor

public class BillingNotificationRequest {


        private String           eventId;
        private BillingEventType eventType;
        private String           tenantId;
        private String           resourceId;
        private int              quantity;
        private BigDecimal       unitPrice;
        private BigDecimal totalAmount;
        private String           currency = "USD";
        private LocalDateTime occurredAt;
        private String           correlationId;



        public BillingNotificationRequest(BillingEventType billingEventType, String tenantId, String messageId, int count, BigDecimal smsPrice, Object correlationId) {
                this.eventId = eventId;
                this.tenantId = tenantId;
                this.resourceId = resourceId;
                this.currency = currency;


        }

        public BillingNotificationRequest(String eventType, String clientId, String phone, String correlationId) {
        }
}
