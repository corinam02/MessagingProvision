package com.example.messageprovisioning.provisioning.model.repository;

import com.example.messageprovisioning.provisioning.model.model.NumberStatus;
import com.example.messageprovisioning.provisioning.model.model.PhoneNumber;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, String> {
   List <PhoneNumber>  findFirstByStatusAndAreaCode(NumberStatus status, String areaCode);
    List<PhoneNumber> findByClientId(String clientId);
}
