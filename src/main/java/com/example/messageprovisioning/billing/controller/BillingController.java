package com.example.messageprovisioning.billing.controller;

import com.example.messageprovisioning.billing.model.BillingRecord;
import com.example.messageprovisioning.billing.service.BillingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/billing")
public class BillingController {

    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    // GET /api/v1/billing/uber/total
    @GetMapping("/{tenantId}/total")
    public ResponseEntity<String> getTotal(@PathVariable String tenantId) {
        BigDecimal total = billingService.getMonthlyTotal(tenantId);
        return ResponseEntity.ok("The total amount for this month "
                + tenantId + ": $" + total);
    }

    @GetMapping("/{tenantId}/records")
    public ResponseEntity<List<BillingRecord>> getRecords(
            @PathVariable String tenantId,
            @RequestParam(defaultValue = "0") int month,
            @RequestParam(defaultValue = "0") int year) {

        if (month == 0) month = LocalDate.now().getMonthValue();
        if (year == 0) year = LocalDate.now().getYear();

        return ResponseEntity.ok(
                billingService.getMonthlyRecords(tenantId, month, year));
    }

    @PostMapping("/internal/notify")
    public ResponseEntity<Void> notify(
            @RequestBody com.example.messageprovisioning.billing.dto.BillingNotificationRequest notification) {
        billingService.recordCharge(notification);
        return ResponseEntity.ok().build();
    }
}