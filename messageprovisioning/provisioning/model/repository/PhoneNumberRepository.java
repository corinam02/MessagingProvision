package com.example.messageprovisioning.provisioning.model.repository;

import com.example.messageprovisioning.provisioning.model.model.NumberStatus;
import com.example.messageprovisioning.provisioning.model.model.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, String> {
    Optional<PhoneNumber>  findFirstByStatusAndAreaCode(NumberStatus status, String areaCode);
    List<PhoneNumber> findByClientId(String clientId);
 }
