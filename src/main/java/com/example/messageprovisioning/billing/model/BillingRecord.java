package com.example.messageprovisioning.billing.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "billing_records", indexes = {
        @Index(name = "idx_bill_tenant",  columnList = "tenant_id"),
        @Index(name = "idx_bill_event",   columnList = "event_id", unique = true)
})
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "event_id", unique = true, nullable = false)
    private String eventId;

    @Column(name = "event_type", nullable = false, length = 30)
    private String eventType;

    @Column(name = "tenant_id", nullable = false, length = 100)
    private String tenantId;

    @Column(name = "resource_id", length = 100)
    private String resourceId;
    @Column(nullable = false)
    private int quantity;

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 6)
    private BigDecimal unitPrice;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 4)
    private BigDecimal totalAmount;

    @Column(nullable = false, length = 3)
    private String currency = "USD";

    @Column(name = "occurred_at", nullable = false)
    private LocalDateTime occurredAt;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt = LocalDateTime.now();
}