package com.example.messageprovisioning.provisioning.model.repository;

import com.example.messageprovisioning.provisioning.model.model.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, UUID> {
}
