package com.metricix.engine.controller;

import com.metricix.engine.dto.TelemetryEventRequest;
import com.metricix.engine.service.TelemetryIngestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Slf4j
@Profile("reactive")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
public class TelemetryController {

    private final TelemetryIngestionService ingestionService;

    public TelemetryController(TelemetryIngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @PostMapping("/track")
    public Mono<ResponseEntity<Map<String, String>>> trackEvent(
            @RequestHeader(value = "X-API-Key", required = false) String apiKey,
            @RequestBody TelemetryEventRequest request) {

        if (apiKey == null || !apiKey.startsWith("mtx_pub_")) {
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid or missing X-API-Key")));
        }

        long start = System.currentTimeMillis();

        return ingestionService.bufferEvent(apiKey, request)
                .doOnSuccess(v -> {
                    long time = System.currentTimeMillis() - start;
                    log.info("Reactive request took {} ms", time);
                })
                .then(Mono.just(ResponseEntity.accepted().body(Map.of(
                        "status", "buffered",
                        "timestamp", Instant.now().toString()
                ))));

    }

    @GetMapping("/events")
    public Mono<ResponseEntity<Flux<Map<String, Object>>>> getEvents(
            @RequestHeader(value = "X-API-Key", required = false) String apiKey,
            @RequestParam(value = "limit", defaultValue = "50") int limit) {

        if (apiKey == null || !apiKey.startsWith("mtx_pub_")) {
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }

        // Using the correctly named ingestionService variable here!
        Flux<Map<String, Object>> eventsStream = ingestionService.getRecentEvents(apiKey, limit);
        return Mono.just(ResponseEntity.ok(eventsStream));
    }

    @GetMapping("/tenants")
    public Mono<List<String>> getTenants() {
        // .collectList() ensures the results are wrapped in [ "id1", "id2" ]
        return ingestionService.getAllTenants().collectList();
    }

    @DeleteMapping("/purge")
    public Mono<ResponseEntity<Map<String, Object>>> purgeData(
            @RequestHeader(value = "X-API-Key", required = false) String apiKey) {

        // 1. Safety Check: Don't allow purging without a valid-looking key
        if (apiKey == null || !apiKey.startsWith("mtx_pub_")) {
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }

        // 2. Execute the purge and return the count of deleted events
        return ingestionService.purgeTenantData(apiKey)
                .map(count -> ResponseEntity.ok().body(Map.of(
                        "status", "deleted",
                        "tenant_purged", apiKey,
                        "rows_removed", count
                )));
    }
}