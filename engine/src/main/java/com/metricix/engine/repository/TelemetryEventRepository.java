package com.metricix.engine.repository;

import com.metricix.engine.entity.TelemetryEvent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface TelemetryEventRepository extends JpaRepository<TelemetryEvent, UUID> {

    // ✅ Fetch recent events for tenant
    List<TelemetryEvent> findByTenantIdAndIsDeletedFalseOrderByCreatedAtDesc(String tenantId, Pageable pageable);

    // ✅ Fetch all active events
    List<TelemetryEvent> findByIsDeletedFalse();

    // ✅ Fetch distinct tenant IDs
    @Query("SELECT DISTINCT e.tenantId FROM TelemetryEvent e WHERE e.isDeleted = false")
    List<String> findAllDistinctTenantIds();

    // ✅ Soft delete tenant data
    @Modifying
    @Transactional
    @Query("UPDATE TelemetryEvent e SET e.isDeleted = true WHERE e.tenantId = :tenant")
    int softDeleteByTenant(@Param("tenant") String tenantId);

}
