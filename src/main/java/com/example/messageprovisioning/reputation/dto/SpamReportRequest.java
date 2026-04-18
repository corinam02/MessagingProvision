package com.example.messageprovisioning.reputation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpamReportRequest {

    @NotBlank(message = "phone number is required")
    private String phoneNumber;

    @NotBlank(message = "reportedId is required")
    private String reporterId;

    @NotBlank(message = "reportType is required")
    @Pattern(regexp = "SPAM|FRAUD|ROBOCALL|HARASSMENT",
    message = "accepted values: SPAM, FRAUD, ROBOCALL, HARRASMENT")
    private String reportType;


}
