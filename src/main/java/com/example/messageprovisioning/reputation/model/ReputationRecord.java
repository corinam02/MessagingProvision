package com.example.messageprovisioning.reputation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table( name = "reputation_record", indexes = {
        @Index(name = "index_rep_number", columnList = "phone_number")
})
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReputationRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "phone_number", nullable = false, length = 10)
    private String phoneNumber;

    @Column(name = "reporter_id", length =20)
    private String reporterId;

    @Column(name = "report_type", nullable = false, length = 30)
    private String reportType;

    @Column(name = "reported_At", nullable = false)
    private LocalDateTime reportedAt = LocalDateTime.now();

    public ReputationRecord(@NotBlank(message = "phone number is required") String phoneNumber, @NotBlank(message = "reportedId is required") String reporterId, @NotBlank(message = "reportType is required") @Pattern(regexp = "SPAM|FRAUD|ROBOCALL|HARASSMENT",
    message = "accepted values: SPAM, FRAUD, ROBOCALL, HARRASMENT") String reportType) {
        this.phoneNumber = phoneNumber;
        this.reporterId = reporterId;
        this.reportType = reportType;
    }

    public void setDisputed(boolean b) {

    }
}
