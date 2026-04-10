package com.example.messageprovisioning.provisioning.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProvisioningRequest {
    @NotBlank(message = "tenantID is required")
    private String tenantId;
}
