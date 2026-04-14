package com.example.messageprovisioning.provisioning.model.dto;

import com.example.messageprovisioning.provisioning.model.model.PhoneNumber;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
public class ProvisioningResponse {

    private String numberId;
    private String phoneNumber;
    private String status;
    private String clientId;
    private LocalDateTime provisionedAt;
    private String message;

    public ProvisioningResponse(PhoneNumber phoneNumber1){
        this.numberId = String.valueOf(phoneNumber1.getId());
        this.phoneNumber = phoneNumber1.getNumber();
        this.status = phoneNumber1.getStatus().name();
        this.clientId = phoneNumber1.getClientId();
        this.provisionedAt = phoneNumber1.getProvisionedAt();
        this.message = "Number" + phoneNumber1.getNumber() + "successfully provisioned to client" + phoneNumber1.getClientId();

    }
}
