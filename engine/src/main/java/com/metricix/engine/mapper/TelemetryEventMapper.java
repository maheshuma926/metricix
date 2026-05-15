package com.metricix.engine.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metricix.engine.dto.TelemetryEventRequest;
import com.metricix.engine.entity.TelemetryEvent;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class TelemetryEventMapper {
    private final ObjectMapper objectMapper;

    public TelemetryEventMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public TelemetryEvent mapToEntity(String apiKey, TelemetryEventRequest request) {

        TelemetryEvent event = new TelemetryEvent();

        event.setTenantId(apiKey);
        event.setEventType(request.event_type());
        event.setUrl(request.url());

        try {
            String payloadJson = objectMapper.writeValueAsString(request.payload());
            event.setPayload(payloadJson);
        } catch (Exception e) {
            throw new RuntimeException("Payload serialization error", e);
        }

        event.setCreatedAt(Instant.now());
        event.setDeleted(false);

        return event;
    }
}
