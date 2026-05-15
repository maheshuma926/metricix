package com.metricix.engine.service;

import com.metricix.engine.dto.TelemetryEventRequest;

public interface TelemetryIngestionServiceBlocking {

    void bufferEvent(String apiKey, TelemetryEventRequest request);

    /*List<String> getAllTenants();

    List<Map<String, Object>> getRecentEvents(String tenantId, int limit);

    long purgeTenantData(String tenantId);*/
}
