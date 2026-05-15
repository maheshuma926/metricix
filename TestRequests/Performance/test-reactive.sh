#!/bin/bash

URL="http://localhost:8080/api/v1/track"
TOTAL_REQUESTS=5000
CONCURRENT_BATCH=200

echo "Starting Reactive API Test..."
echo "Total Requests: $TOTAL_REQUESTS"
echo "Concurrency Batch: $CONCURRENT_BATCH"

start_time=$(date +%s%3N)

for ((i=1; i<=TOTAL_REQUESTS; i++))
do
  curl -s -X POST "$URL" \
    -H "Content-Type: application/json" \
    -H "X-API-Key: mtx_pub_test123" \
    -d '{"event_type":"test"}' > /dev/null &

  # Control concurrency
  if (( i % CONCURRENT_BATCH == 0 )); then
    wait
  fi
done

wait

end_time=$(date +%s%3N)
duration=$((end_time - start_time))

echo "--------------------------------------"
echo "Reactive Test Completed ✅"
echo "Total Requests: $TOTAL_REQUESTS"
echo "Total Time: ${duration} ms"
echo "Average Time per Request: $((duration / TOTAL_REQUESTS)) ms"
echo "Requests/sec: $((TOTAL_REQUESTS * 1000 / duration))"