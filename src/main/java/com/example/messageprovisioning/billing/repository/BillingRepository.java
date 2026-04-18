package com.example.messageprovisioning.billing.repository;

import com.example.messageprovisioning.billing.model.BillingRecord;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface BillingRepository extends JpaRepository<BillingRecord, String> {


    boolean existsByEventId(String eventId);


    @Query("SELECT b FROM BillingRecord b " +
            "WHERE b.tenantId = :tenantId " +
            "AND MONTH(b.occurredAt) = :month " +
            "AND YEAR(b.occurredAt) = :year " +
            "ORDER BY b.occurredAt ASC")
    List<BillingRecord> findByTenantIdAndMonth(
            @Param("tenantId") String tenantId,
            @Param("month")    int month,
            @Param("year")     int year);


    @Query("SELECT COALESCE(SUM(b.totalAmount), 0) FROM BillingRecord b " +
            "WHERE b.tenantId = :tenantId " +
            "AND MONTH(b.occurredAt) = :month " +
            "AND YEAR(b.occurredAt) = :year")
    BigDecimal sumByTenantAndMonth(
            @Param("tenantId") String tenantId,
            @Param("month")    int month,
            @Param("year")     int year);
}
