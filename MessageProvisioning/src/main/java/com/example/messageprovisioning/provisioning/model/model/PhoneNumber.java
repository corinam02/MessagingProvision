package com.example.messageprovisioning.provisioning.model.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Column(nullable = false)
    private NumberStatus status = NumberStatus.AVAILABLE;

}
