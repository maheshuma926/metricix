package com.metricix.engine.service;

import com.metricix.engine.entity.TelemetryEvent;
import com.metricix.engine.repository.TelemetryEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TelemetryAsyncService {

    private final TelemetryEventRepository repository;

    public TelemetryAsyncService(TelemetryEventRepository repository) {
        this.repository = repository;
    }

    @Async
    public void saveEventAsync(TelemetryEvent entity) {
        log.info("Thread: {}", Thread.currentThread());
        repository.save(entity);
        log.debug("Event saved async for tenant {}", entity.getTenantId());
    }
}
