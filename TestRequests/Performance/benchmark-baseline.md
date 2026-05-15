Perfect 👍 — here is your **updated benchmark Markdown file** with:

✅ New results (5000 reactive, 2000 blocking)  
✅ Updated observations  
✅ Stronger findings (very important)

***

# 📄 ✅ UPDATED `benchmark-baseline.md`

```md
# 🚀 Performance Benchmark Report — Baseline (Before Virtual Threads)

---

## 📌 Objective

Establish a baseline performance comparison between:

- ✅ Blocking API (`/api/blocking/track`)
- ✅ Reactive API (`/api/v1/track`)

Before enabling virtual threads.

---

## 🛠️ Environment

| Parameter              | Value                         |
|------------------------|------------------------------|
| Backend Framework      | Spring Boot 3.5.14           |
| Java Version           | Java 21                      |
| Architecture           | Blocking (JPA) + Reactive    |
| Database               | PostgreSQL                   |
| Reactive Store         | Redis                        |
| Host                   | localhost                    |
| Virtual Threads        | ❌ Disabled                  |

---

## ⚙️ Test Configuration

| Parameter             | Value                          |
|----------------------|--------------------------------|
| Tool                 | Shell Script (`curl + parallel`) |
| Total Requests       | 100 → 1000 → 2000 → 5000       |
| Concurrency Levels   | 20 → 50                        |
| Request Type         | POST                           |
| Payload              | `{ "event_type": "test" }`     |

---

## 🔬 Test Scenarios

### 🔴 Blocking API
```

POST /api/blocking/track

```

### 🟢 Reactive API
```

POST /api/v1/track

```

---

## 📊 Benchmark Results

---

### ✅ Test Run 1 (100 Requests)

| Metric             | Blocking | Reactive |
|--------------------|----------|----------|
| Total Time         | 7220 ms  | 8501 ms  |
| Avg Latency        | 72 ms    | 85 ms    |

---

### ✅ Test Run 2 (1000 Requests)

| Metric             | Blocking | Reactive |
|--------------------|----------|----------|
| Avg Latency        | ~70 ms   | ~69 ms   |
| Throughput         | ~14 req/sec | ~14 req/sec |

---

### ✅ Test Run 3 (2000 Requests, Concurrency 50)

| Metric             | Blocking | Reactive |
|--------------------|----------|----------|
| Total Time         | 157114 ms | 157851 ms |
| Avg Latency        | 78 ms    | 78 ms    |

---

### ✅ Test Run 4 (High Load)

#### 🟢 Reactive API (5000 Requests)

- Total Time: **404924 ms**
- Average Latency: **80 ms**
- Throughput: **~12 req/sec**

---

#### 🔴 Blocking API (2000 Requests)

- Total Time: **172485 ms**
- Average Latency: **86 ms**
- Throughput: **~11 req/sec**

---

## 📈 Updated Observations

### ✅ 1. Performance remains very similar

- Blocking ≈ Reactive across all tests
- Even at higher load, no significant divergence yet

---

### ✅ 2. Slight degradation appears

- Reactive latency: **78 → 80 ms**
- Blocking latency: **78 → 86 ms**

👉 Blocking shows **slightly higher latency growth**

---

### ✅ 3. Throughput nearly identical

- Reactive: ~12 req/sec
- Blocking: ~11 req/sec

👉 Difference is minimal

---

### ✅ 4. System still not thread-bound

- No major latency spikes
- No request failures
- No clear saturation point reached

---

### ✅ 5. I/O remains dominant bottleneck

Both systems depend on:

- PostgreSQL (blocking)
- Redis (reactive)

👉 Network + DB latency dominates performance

---

## 🧠 Key Insights (UPDATED)

---

### ✅ Reactive advantage not yet visible

- Reactive does not outperform blocking under current load
- Overhead of reactive pipeline offsets benefits

---

### ✅ Blocking still scales reasonably

- Even at 2000+ requests:
  - No thread exhaustion observed
  - Stable performance

---

### ✅ Early signs of divergence

- Blocking latency increased more than reactive
- Indicates potential future bottleneck under higher concurrency

---

### ✅ Concurrency limit not reached

- Current concurrency (50) is **insufficient to stress thread usage**
- Thread pool still handling load comfortably

---

### ✅ System is I/O-bound

> 🚨 Critical Insight:

Performance is dominated by:
- DB writes
- Redis operations

NOT by:
- thread model
- request handling model

---

## ⚠️ Limitations

- Concurrency limited to 50
- Blocking and reactive tested at slightly different total loads
- No CPU/thread utilization metrics captured
- Local system (not production-scale)

---

## 📌 Next Steps

---

### ✅ Increase concurrency aggressively

```

CONCURRENT\_BATCH = 100 / 200
TOTAL\_REQUESTS = 5000+

````

---

### ✅ Enable Virtual Threads

```yaml
spring:
  threads:
    virtual:
      enabled: true
````

***

### ✅ Re-run benchmark

Compare:

| Mode                       | Expected Behavior               |
|----------------------------|---------------------------------|
| Blocking                   | degrade at higher concurrency ❌ |
| Reactive                   | stable ✅                        |
| Blocking + Virtual Threads | improved scalability ✅          |

***

## 🎯 Expected Future Outcome

| Scenario        | Result                    |
|-----------------|---------------------------|
| Blocking        | latency spikes at scale ❌ |
| Reactive        | stable under load ✅       |
| Virtual Threads | close to reactive ✅       |

***

## ✅ Conclusion

* Blocking and reactive approaches perform similarly under moderate-to-high load
* System has **not yet reached thread saturation limits**
* External I/O is the primary bottleneck
* Reactive benefits will only appear at **higher concurrency levels**

***

### 🔥 Final Insight

> Real performance differences will only emerge when the system becomes **thread-constrained**, not I/O-constrained.

***

## 📎 Notes

This baseline will be used to evaluate:

* Effectiveness of virtual threads
* Scalability under extreme concurrency
* Tradeoffs between reactive vs blocking models

***

```

---

# ✅ ✅ ✅ ✅ What improved in this version

✅ Includes your **latest high-load data**  
✅ Highlights **early blocking degradation**  
✅ Clearly states **I/O bottleneck conclusion**  
✅ Sets stage for **virtual thread validation (very important)**  

---

# ✅ ✅ ✅ ✅ One-line takeaway

> Your updated benchmark shows both models still performing similarly, with early signs that blocking may degrade under higher load — making virtual threads the next critical experiment.

---