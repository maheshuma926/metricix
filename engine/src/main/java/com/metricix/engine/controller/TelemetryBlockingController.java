package com.metricix.engine.controller;

import com.metricix.engine.dto.TelemetryEventRequest;
import com.metricix.engine.service.TelemetryIngestionServiceBlocking;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@CrossOrigin(origins = "*")
@Profile("blocking")
@RestController
@RequestMapping("/api/blocking")
public class TelemetryBlockingController {

    private final TelemetryIngestionServiceBlocking telemetryIngestionServiceBlocking;

    public TelemetryBlockingController(TelemetryIngestionServiceBlocking telemetryIngestionServiceBlocking) {
        this.telemetryIngestionServiceBlocking = telemetryIngestionServiceBlocking;
    }

    @PostMapping("/track")
    public ResponseEntity<Map<String, String>> trackEvent(
            @RequestHeader(value = "X-API-Key", required = false) String apiKey,
            @RequestBody TelemetryEventRequest request) {

        if (apiKey == null || !apiKey.startsWith("mtx_pub_")) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid API key"));
        }

        telemetryIngestionServiceBlocking.bufferEvent(apiKey, request);

        return ResponseEntity
                .accepted()
                .body(Map.of("status", "buffered"
                ));
    }

    /*@GetMapping("/events")
    public ResponseEntity<List<Map<String, Object>>> getEvents(
            @RequestHeader("X-API-Key") String apiKey,
            @RequestParam(defaultValue = "50") int limit) {

        List<Map<String, Object>> events =
                telemetryIngestionService.getRecentEvents(apiKey, limit);

        return ResponseEntity.ok(events);
    }

    @GetMapping("/tenants")
    public ResponseEntity<List<String>> getTenants() {
        return ResponseEntity.ok(telemetryIngestionService.getAllTenants());
    }

    @DeleteMapping("/purge")
    public ResponseEntity<Map<String, Object>> purge(
            @RequestHeader("X-API-Key") String apiKey) {

        long count = telemetryIngestionService.purgeTenantData(apiKey);

        return ResponseEntity.ok(Map.of(
                "status", "deleted",
                "rows_removed", count
        ));
    }*/
}
