package com.example.messageprovisioning.provisioning.model.dto;

import com.example.messageprovisioning.provisioning.model.model.PhoneNumber;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.processing.Pattern;
import jakarta.validation.constraints.*;

@Getter @Setter
@NoArgsConstructor
public class ProvisioningRequest {
    @NotBlank(message = "tenantID is required")
    private String clientId;

    @NotBlank(message = "requestedby is required")
    @Email(message = "email must be valid")
    private String requestedBy;

    @NotBlank(message = "areaCode is required")
    private String areaCode;

    public ProvisioningRequest(String clientId, String requestedBy, String areaCode){
        this.clientId = clientId;
        this.requestedBy = requestedBy;
        this.areaCode = areaCode;
    }

    public ProvisioningRequest(PhoneNumber phoneNumber) {
    }
}
