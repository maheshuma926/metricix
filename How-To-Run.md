# 📄 How-To-Run.md

````markdown
# 🚀 How to Run Metricix (Backend + Frontend)

This guide explains how to run the **Event Ingestion Simulator (Angular UI)** and the **Spring Boot backend service**.

---

# 📦 Prerequisites

Make sure you have the following installed:

## ✅ Backend
- Java 17+
- Maven or Gradle

## ✅ Frontend
- Node.js (>= 16)
- npm (or yarn)
- Angular CLI

```bash
npm install -g @angular/cli
````

***

# 🧠 Project Overview

    Frontend (Angular) → Event Simulator UI
    Backend (Spring Boot) → Event Ingestion API

***

# ⚙️ 1. Run Backend (Spring Boot)

## 📁 Navigate to backend project

```bash
cd metricix-backend
```

***

## ▶️ Start the application

### If using Maven:

```bash
./mvnw spring-boot:run
```

### Or:

```bash
mvn spring-boot:run
```

***

### ✅ Backend starts at:

    http://localhost:8080

***

### 📡 Key API Endpoint

    POST http://localhost:8080/api/blocking/track

Headers:

    X-API-Key: mtx_pub_test_123

Example body:

```json
{
  "event_type": "button_click",
  "payload": {
    "button_id": "signup_hero",
    "path": "/home"
  }
}
```

***

## ✅ Check backend is running

Open browser:

    http://localhost:8080

or test with curl:

```bash
curl -X POST http://localhost:8080/api/blocking/track \
  -H "Content-Type: application/json" \
  -H "X-API-Key: mtx_pub_test_123" \
  -d '{"event_type":"test","payload":{}}'
```

***

# 🌐 2. Run Frontend (Angular UI)

## 📁 Navigate to UI project

```bash
cd metricix-ui
```

***

## 📥 Install dependencies

```bash
npm install
```

***

## ▶️ Start Angular app

```bash
ng serve
```

***

### ✅ Frontend runs at:

    http://localhost:4200

***

# 🔗 3. Connect Frontend to Backend

Make sure your Angular app is pointing to:

    http://localhost:8080/api/blocking/track

Example:

```ts
API_URL = 'http://localhost:8080/api/blocking/track';
```

***

# 🌍 4. Enable CORS (Backend)

Since frontend runs on port 4200, enable CORS in Spring Boot:

```java
@CrossOrigin(origins = "http://localhost:4200")
```

OR global config:

```java
registry.addMapping("/**")
        .allowedOrigins("http://localhost:4200")
        .allowedMethods("*");
```

***

# ✅ 5. Test the Flow

1. Open:
   http://localhost:4200

2. Go to:
   Event Simulator

3. Fill:
    * API Key
    * Event Type
    * JSON Payload

4. Click:
   Simulate Event

***

# 📊 Expected Output

You should see logs in the **Live Console**:

    [12:45:21] > POST http://localhost:8080/api/blocking/track

    {
      "event_type": "button_click"
    }

    [12:45:22] < 202 Accepted

***

# 🛠 Common Issues

## ❌ Backend not reachable

    ERR_CONNECTION_REFUSED

✅ Fix:

* Ensure backend is running on port 8080

***

## ❌ CORS error

    Blocked by CORS policy

✅ Fix:

* Add `@CrossOrigin` config

***

## ❌ HttpClient error

    No provider for HttpClient

✅ Fix:

* Add in `app.config.ts`

```ts
provideHttpClient()
```

***

## ❌ UI not styled (Bootstrap missing)

✅ Fix:

In `angular.json`:

```json
"styles": [
  "node_modules/bootstrap/dist/css/bootstrap.min.css"
]
```

***

# ✅ Final Architecture

    Angular UI (4200)
            ↓
    Spring Boot API (8080)
            ↓
    Event Processing / DB

***

# 🎯 You're Ready!

You now have:

✅ Event simulator UI  
✅ Live console logging  
✅ Real API integration  
✅ Full end-to-end test flow

***

# 🚀 Next Improvements (Optional)

* Add batch event simulation
* Add request latency tracking
* Add real-time dashboard

***

```

---
