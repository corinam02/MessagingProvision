package com.example.messageprovisioning.billing.service;

import com.example.messageprovisioning.billing.dto.BillingNotificationRequest;
import com.example.messageprovisioning.billing.repository.BillingRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.messageprovisioning.billing.dto.BillingNotificationRequest;
import com.example.messageprovisioning.billing.model.BillingRecord;
import com.example.messageprovisioning.billing.repository.BillingRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class BillingService {

    private static final Logger log = LoggerFactory.getLogger(BillingService.class);

    private final BillingRepository billingRepository;

    public BillingService(BillingRepository billingRepository) {
        this.billingRepository = billingRepository;
    }

    @Transactional
    public void recordCharge(BillingNotificationRequest notification) {
        // Deduplicare: daca am mai procesat acest eveniment, il ignoram
        // SQS garanteaza "cel putin o data" — deci pot veni duplicate
        if (billingRepository.existsByEventId(notification.getEventId())) {
            log.warn("Eveniment billing duplicat ignorat: eventId={}",
                    notification.getEventId());
            return;
        }
        BillingRecord record = new BillingRecord();
        record.setEventId(notification.getEventId());
        record.setEventType(notification.getEventType().name());
        record.setTenantId(notification.getTenantId());
        record.setResourceId(notification.getResourceId());
        record.setQuantity(notification.getQuantity());
        record.setUnitPrice(notification.getUnitPrice());
        record.setTotalAmount(notification.getTotalAmount());
        record.setCurrency(notification.getCurrency());
        record.setOccurredAt(notification.getOccurredAt());

        billingRepository.save(record);

        log.info("Taxa inregistrata: tenant={}, tip={}, total={}",
                notification.getTenantId(), notification.getEventType(),
                notification.getTotalAmount());
    }

    public BigDecimal getMonthlyTotal(String tenantId) {
        int month = LocalDate.now().getMonthValue();
        int year  = LocalDate.now().getYear();
        return billingRepository.sumByTenantAndMonth(tenantId, month, year);
    }

    public List<BillingRecord> getMonthlyRecords(String tenantId, int month, int year) {
        return billingRepository.findByTenantIdAndMonth(tenantId, month, year);
    }
}
