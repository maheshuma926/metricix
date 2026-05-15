package com.metricix.engine.service.impl;

import com.metricix.engine.dto.TelemetryEventRequest;
import com.metricix.engine.entity.TelemetryEvent;
import com.metricix.engine.mapper.TelemetryEventMapper;
import com.metricix.engine.service.TelemetryAsyncService;
import com.metricix.engine.service.TelemetryIngestionServiceBlocking;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("blocking")
@Slf4j
@Service
public class TelemetryIngestionServiceBlockingImpl implements TelemetryIngestionServiceBlocking {

    private final TelemetryAsyncService asyncService;
    private final TelemetryEventMapper mapper;

    public TelemetryIngestionServiceBlockingImpl(TelemetryAsyncService asyncService, TelemetryEventMapper mapper) {
        this.asyncService = asyncService;
        this.mapper = mapper;
    }

    @Override
    public void bufferEvent(String apiKey, TelemetryEventRequest request) {
        TelemetryEvent entity = mapper.mapToEntity(apiKey, request);

        // ✅ async call
        asyncService.saveEventAsync(entity);

        log.debug("Event saved for tenant {}", apiKey);
    }

    /*// ✅ GET TENANTS
    @Override
    public List<String> getAllTenants() {
        return repository.findAllDistinctTenantIds();
    }

    // ✅ GET EVENTS
    @Override
    public List<Map<String, Object>> getRecentEvents(String tenantId, int limit) {

        var pageable = org.springframework.data.domain.PageRequest.of(0, limit);

        List<TelemetryEvent> events =
                repository.findByTenantIdAndIsDeletedFalseOrderByCreatedAtDesc(
                        tenantId, pageable
                );

        // Convert entity → map (for now, keep it simple)
        return events.stream()
                .map(event -> Map.of(
                        "id", event.getId(),
                        "tenant_id", event.getTenantId(),
                        "event_type", event.getEventType(),
                        "url", event.getUrl(),
                        "payload", event.getPayload(), // JSON string
                        "created_at", event.getCreatedAt()
                ))
                .toList();
    }

    // ✅ PURGE (soft delete)
    @Override
    @Transactional
    public long purgeTenantData(String tenantId) {

        log.info("Soft deleting events for tenant {}", tenantId);

        return repository.softDeleteByTenant(tenantId);
    }*/
}