# Engine Backend Documentation and Problem Review

## Backend Overview (`engine/`)

- **Framework/runtime:** Spring Boot 3.5.14 + Java 21 (`build.gradle`)
- **Reactive stack:** WebFlux + Reactive Redis + R2DBC PostgreSQL
- **Persistence flow:** `POST /track` buffers to Redis list `metricix_ingest_queue`, scheduled sweeper writes into
  PostgreSQL table `metricix_events`
- **Schema management:** Flyway migrations (`V1__init_schema.sql`, `V2__add_soft_delete_column.sql`)
- **Soft delete model:** `is_deleted` flag used for reads and purge operations

## Code Map

- `EngineApplication.java`
    - Enables scheduling with `@EnableScheduling`.
- `TelemetryController.java`
    - `POST /api/v1/track` validates key prefix and queues event.
    - `GET /api/v1/events` returns recent events for tenant.
    - `GET /api/v1/tenants` lists active tenant IDs.
    - `DELETE /api/v1/purge` soft-deletes tenant events.
- `TelemetryIngestionService.java`
    - Decorates incoming event (`tenant_id`, `received_at`) and pushes JSON into Redis queue.
    - Reads tenants/events from PostgreSQL.
    - Implements soft-delete purge update.
- `TelemetrySweeper.java`
    - Runs every `BATCH_INTERVAL_MS` (default 5000 ms).
    - Renames ingest queue to processing queue, reads list, inserts each event into DB.
    - Deletes processing queue after insert pipeline completes.

## API Contracts (As Implemented)

### `POST /api/v1/track`

- Required header: `X-API-Key` starting with `mtx_pub_`
- Body: `TelemetryEventRequest(event_type, url, payload)`
- Success: `202 Accepted` with `{ status: "buffered", timestamp: ... }`

### `GET /api/v1/events?limit=50`

- Required header: `X-API-Key` with `mtx_pub_` prefix
- Returns a stream/list of recent non-deleted events for that tenant

### `GET /api/v1/tenants`

- No header required
- Returns distinct active tenant IDs (`is_deleted = FALSE`)

### `DELETE /api/v1/purge`

- Required header: `X-API-Key` with `mtx_pub_` prefix
- Marks all rows for tenant as `is_deleted = TRUE`

## Verified Problems and Risks

### High Severity

1. **Processing queue can get stuck permanently after DB error**
    - **Where:** `TelemetrySweeper.java` lines 41-49, 75-81
    - **Why:** Queue is renamed to `metricix_processing_queue`; if insert errors, delete step is skipped and key can
      remain forever. Next cycles only check ingest key, not existing processing key.
    - **Impact:** Backlog can stall and new renames may fail due key collision, effectively halting ingestion.

2. **Silent event loss on parse/insert conversion issues**
    - **Where:** `TelemetrySweeper.java` lines 58-73, 75-76
    - **Why:** Parse failures return `Mono.empty()` (event dropped), then processing queue is deleted after loop.
    - **Impact:** Bad records are discarded with no DLQ/retry path.

3. **Authentication is only a prefix check**
    - **Where:** `TelemetryController.java` lines 31-34, 48-50, 66-68
    - **Why:** Any string starting with `mtx_pub_` is accepted.
    - **Impact:** No real API key verification; easy spoofing/tenant impersonation.

### Medium Severity

4. **No input validation for required payload fields**
    - **Where:** `TelemetryEventRequest.java`, `TelemetryController.java` line 29
    - **Why:** No Bean Validation annotations (`@NotBlank`, `@NotNull`) and no `@Valid`.
    - **Impact:** Null/invalid values can reach DB pipeline and fail later.

5. **Open CORS for all origins**
    - **Where:** `TelemetryController.java` line 15
    - **Why:** `@CrossOrigin(origins = "*")`
    - **Impact:** Browser clients from any origin can call API with exposed keys.

6. **Hard-coded secrets in source-controlled config**
    - **Where:** `application.yml` lines 8-9, 14-15; `docker-compose.yml` lines 23, 47, 50
    - **Impact:** Credential leakage risk and unsafe defaults.

7. **Scheduler implementation can overlap / run unmanaged reactive work**
    - **Where:** `TelemetrySweeper.java` lines 35-82
    - **Why:** `@Scheduled` method starts async pipeline with `.subscribe()` and returns immediately.
    - **Impact:** Overlapping runs and race conditions under slow DB/Redis conditions.

### Low Severity

8. **Potentially unbounded `limit` query parameter**
    - **Where:** `TelemetryController.java` line 46, `TelemetryIngestionService.java` lines 60-63
    - **Impact:** Very high limits can pressure DB and memory.

9. **Logging level suppresses useful operational info**
    - **Where:** `application.yml` lines 29-32
    - **Impact:** `INFO` sweeper diagnostics are hidden in normal runs.

10. **Automated test requires external DB connectivity by default**
    - **Where:** `EngineApplicationTests.java`, test run output
    - **Impact:** `./gradlew test` fails without PostgreSQL/Flyway target.

## Validation Note

Executed `./gradlew.bat test --no-daemon` in `engine/` and observed `contextLoads()` failure due PostgreSQL connection
refusal during Flyway initialization (no local DB at configured host/port).

## Recommended Next Fixes (Priority Order)

1. Add robust queue recovery in sweeper (process existing processing queue first; handle rename collisions; never orphan
   queue).
2. Add DLQ for failed/invalid records instead of dropping.
3. Replace prefix-only API key check with real key verification.
4. Add request validation (`@Valid`, Bean Validation) and bounded `limit`.
5. Move secrets to environment variables only; keep safe defaults in config.
6. Restrict CORS to trusted frontend origin(s).
7. Refactor scheduler flow to avoid unmanaged `.subscribe()` race patterns.

