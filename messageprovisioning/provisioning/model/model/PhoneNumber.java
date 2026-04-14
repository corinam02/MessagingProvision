package com.example.messageprovisioning.provisioning.model.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "phone_number", indexes = {
        @Index(name = "id_number", columnList = "number", unique = true)
}
)
@Getter @Setter
@NoArgsConstructor
public class PhoneNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 20)
    private String number;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NumberStatus status;

    @Column(name = "clientId", length = 100)
    private String clientId;

    @Column(name = "areaCode", length = 10, nullable = false)
    private String areaCode;

    @Column(name = "provisioned_At")
    private LocalDateTime provisionedAt;

    @Column(name = "created_At", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public PhoneNumber(String number, String clientId, String areaCode, NumberStatus status) {
        this.number = number;
        this.clientId = clientId;
        this.areaCode = areaCode;
        this.status = NumberStatus.AVAILABLE;
    }
}
