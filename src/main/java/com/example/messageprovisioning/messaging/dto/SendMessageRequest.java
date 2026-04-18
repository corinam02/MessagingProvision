package com.example.messageprovisioning.messaging.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.services.sqs.model.AddPermissionRequest;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageRequest {
    @NotBlank
    @Pattern(regexp = "^\\+[1-9]\\d{7,14}$",
            message = "fromNumber must be E.164: +19195550100")
    private String fromNumber;

    @NotBlank
    @Pattern(regexp = "^\\+[1-9]\\d{7,14}$",
            message = "toNumber must be E.164")
    private String toNumber;

    @NotBlank
    @Size(max = 1600, message = "SMS-ul can't overflow 1600 characters")
    private String body;

    @NotBlank
    private String tenantId;
}
