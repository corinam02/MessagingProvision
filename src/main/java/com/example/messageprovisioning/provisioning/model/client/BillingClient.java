package com.example.messageprovisioning.provisioning.model.client;

import com.example.messageprovisioning.billing.dto.BillingNotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "billing-service",
        url = "http://${services.billing.url:localhost:8082}"
)
public interface BillingClient {

    @PostMapping("/api/v1/billing/internal/notify")
    void notifyProvisioning(@RequestBody BillingNotificationRequest request);
}
