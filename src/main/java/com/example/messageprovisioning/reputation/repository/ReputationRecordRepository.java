package com.example.messageprovisioning.reputation.repository;

import com.example.messageprovisioning.reputation.model.ReputationRecord;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;


public interface ReputationRecordRepository extends JpaRepository<ReputationRecord, String> {
    long countByPhoneNumber(String phoneNumber);

    @Query("SELECT COUNT(r) FROM ReputationRecord  r" +
            "WHERE r.phoneNumber = :phone" +
            "AND r.reportedAt > :since" +
            "AND r.disputed = false")
    long countRecentUndisputedReports(@Param("phone") String phone, @Param("since")LocalDateTime since);


}
