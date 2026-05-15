#!/bin/bash

URL="http://localhost:8080/api/blocking/track"
API_KEY="mtx_pub_test123"
TOTAL_REQUESTS=5000
CONCURRENT_BATCH=50

echo "Starting Blocking API Test..."
start_time=$(date +%s%3N)

for ((i=1; i<=TOTAL_REQUESTS; i++))
do
  curl -s -X POST "$URL" \
    -H "Content-Type: application/json" \
    -H "X-API-Key: $API_KEY" \
    -d '{"event_type":"test"}' &

  # control concurrency
  if (( i % CONCURRENT_BATCH == 0 )); then
    wait
  fi
done

wait

end_time=$(date +%s%3N)
duration=$((end_time - start_time))

echo "--------------------------------------"
echo "Blocking Test Completed ✅"
echo "Total Requests: $TOTAL_REQUESTS"
echo "Total Time: ${duration} ms"
echo "Average Time per Request: $((duration / TOTAL_REQUESTS)) ms"
echo "Requests/sec: $((TOTAL_REQUESTS * 1000 / duration))"
