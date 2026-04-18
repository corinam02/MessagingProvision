package com.example.messageprovisioning.provisioning.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillingNotificationRequest {

    private String eventType;
    private String clientId;
    private String phoneNumber;
    private String correlationId;

}
