# ✈️ Bonvo Backend — Kurulum ve Kullanım Kılavuzu

Spring Boot 3 + Java 21 ile geliştirilmiş Bonvo AI Travel Assistant backend'i.

---

## 📋 Gereksinimler

| Araç | Minimum Versiyon |
|------|-----------------|
| Java JDK | 21 |
| Maven | 3.9+ |
| Docker & Docker Compose | 24+ |
| PostgreSQL (Docker olmadan) | 15+ |

---

## 🚀 Yöntem 1: Docker Compose ile (Tavsiye Edilen)

Her şeyi tek komutla ayağa kaldırır: PostgreSQL + Backend + Frontend

### 1. Ortam değişkenlerini ayarlayın

```bash
cp .env .env
# .env dosyasını açıp OPENAI_API_KEY ve JWT_SECRET değerlerini doldurun
```

### 2. Frontend'i backend klasörüne taşıyın

```
proje/
├── bonvo/              ← React frontend
├── bonvo-backend/      ← Spring Boot backend (bu klasör)
│   └── docker-compose.yml
```

### 3. Docker Compose'u başlatın

```bash
cd bonvo-backend
docker-compose up --build
```

Servisler hazır olduğunda:
- **Frontend:** http://localhost
- **Backend API:** http://localhost:8080
- **H2 Console (dev):** http://localhost:8080/h2-console
- **PostgreSQL:** localhost:5432

### Durdurmak için
```bash
docker-compose down
# Veritabanını da silmek için:
docker-compose down -v
```

---

## 🛠 Yöntem 2: Manuel Geliştirme Ortamı

### 1. PostgreSQL başlatın (Docker ile)

```bash
docker run -d \
  --name bonvo-postgres \
  -e POSTGRES_DB=bonvo \
  -e POSTGRES_USER=bonvo \
  -e POSTGRES_PASSWORD=bonvo123 \
  -p 5432:5432 \
  postgres:16-alpine
```

**Veya H2 ile (herhangi bir kurulum gerekmez — varsayılan dev profili)**

### 2. Ortam değişkenlerini ayarlayın

```bash
export OPENAI_API_KEY=sk-xxxxxxxxxx
export JWT_SECRET=en_az_32_karakter_gizli_anahtar_buraya
# İsteğe bağlı:
export AMADEUS_CLIENT_ID=demo
export AMADEUS_CLIENT_SECRET=demo
```

### 3. H2 (geliştirme) ile başlatın

```bash
cd bonvo-backend
mvn spring-boot:run
```

### 4. PostgreSQL (prod profili) ile başlatın

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### 5. Frontend'i başlatın (ayrı terminal)

```bash
cd bonvo
npm install
npm run dev
# http://localhost:5173
```

---

## 📡 API Referansı

Base URL: `http://localhost:8080/api`

### 🔐 Auth

| Method | Endpoint | Açıklama |
|--------|----------|----------|
| `POST` | `/auth/register` | Yeni kullanıcı kaydı |
| `POST` | `/auth/login` | Giriş → JWT token |

**Login isteği:**
```json
POST /api/auth/login
{
  "email": "ayse@bonvo.ai",
  "password": "password123"
}
```

**Yanıt:**
```json
{
  "token": "eyJhbGc...",
  "type": "Bearer",
  "userId": 1,
  "name": "Ayşe Yılmaz",
  "email": "ayse@bonvo.ai"
}
```

> Sonraki tüm isteklerde header ekleyin:
> `Authorization: Bearer <token>`

---

### ✈️ Geziler

| Method | Endpoint | Açıklama |
|--------|----------|----------|
| `GET` | `/trips` | Tüm gezilerim |
| `GET` | `/trips/{id}` | Gezi detayı |
| `POST` | `/trips` | Yeni gezi oluştur |
| `PATCH` | `/trips/{id}/status` | Gezi durumunu güncelle |
| `DELETE` | `/trips/{id}` | Gezi sil |

**Yeni gezi oluşturma:**
```json
POST /api/trips
{
  "destination": "Tokyo",
  "country": "Japonya",
  "flag": "🇯🇵",
  "startDate": "2025-07-15",
  "endDate": "2025-07-28",
  "type": "CULTURE",
  "budget": 45000,
  "travelers": 2,
  "accommodation": "COMFORT",
  "interests": ["museum", "street_food", "temple"]
}
```

---

### 📚 Kelime Defteri

| Method | Endpoint | Açıklama |
|--------|----------|----------|
| `GET` | `/trips/{id}/vocab` | Tüm kelimeler |
| `GET` | `/trips/{id}/vocab?status=HARD` | Filtreli kelimeler |
| `GET` | `/trips/{id}/vocab/stats` | İstatistikler |
| `POST` | `/trips/{id}/vocab` | Kelime ekle |
| `PATCH` | `/trips/{id}/vocab/{wordId}/status` | Durum güncelle |
| `DELETE` | `/trips/{id}/vocab/{wordId}` | Kelime sil |

**Kelime durumu güncelleme:**
```json
PATCH /api/trips/1/vocab/3/status
{
  "status": "LEARNED"   // LEARNED | LEARNING | HARD
}
```

---

### 🤖 AI Planı (SSE Streaming)

```
GET /api/ai/trips/{id}/plan
Accept: text/event-stream
Authorization: Bearer <token>
```

Frontend'de kullanımı:
```typescript
const eventSource = new EventSource(
  `http://localhost:8080/api/ai/trips/${tripId}/plan`
);
// Not: EventSource JWT desteklemez, token'ı query param olarak gönderebilirsiniz
// veya fetch + ReadableStream kullanın:

const response = await fetch(`/api/ai/trips/${tripId}/plan`, {
  headers: { Authorization: `Bearer ${token}` }
});
const reader = response.body!.getReader();
const decoder = new TextDecoder();
while (true) {
  const { done, value } = await reader.read();
  if (done) break;
  console.log(decoder.decode(value)); // streaming chunk
}
```

---

### ✈️ Uçuş Arama

```json
POST /api/flights/search
{
  "originCode": "IST",
  "destinationCode": "TYO",
  "departureDate": "2025-07-15",
  "returnDate": "2025-07-28",
  "adults": 2,
  "cabinClass": "ECONOMY"
}
```

---

## 🗂 Proje Yapısı

```
bonvo-backend/
├── src/main/java/ai/bonvo/
│   ├── BonvoApplication.java
│   ├── config/
│   │   ├── SecurityConfig.java      ← JWT + CORS + Spring Security
│   │   └── WebClientConfig.java     ← Amadeus / Booking API clients
│   ├── controller/
│   │   ├── AuthController.java      ← /api/auth/**
│   │   ├── TripController.java      ← /api/trips/**
│   │   ├── VocabController.java     ← /api/trips/{id}/vocab/**
│   │   ├── AiController.java        ← /api/ai/** (SSE)
│   │   ├── FlightController.java    ← /api/flights/**
│   │   ├── UserController.java      ← /api/users/me
│   │   └── HealthController.java    ← /api/health
│   ├── dto/                         ← Request / Response nesneleri
│   ├── entity/                      ← JPA varlıkları (User, Trip, VocabWord)
│   ├── exception/                   ← Global hata yönetimi
│   ├── repository/                  ← Spring Data JPA arayüzleri
│   ├── security/                    ← JwtUtils + JwtAuthFilter
│   └── service/                     ← İş mantığı katmanı
├── src/main/resources/
│   ├── application.yml              ← Ortak konfigürasyon (H2 dev)
│   ├── application-prod.yml         ← PostgreSQL konfigürasyonu
│   └── data.sql                     ← Dev seed verisi
├── Dockerfile                       ← Backend container
├── docker-compose.yml               ← Tam yığın
├── nginx.conf                       ← Frontend + API proxy
└── .env.example                     ← Ortam değişkenleri şablonu
```

---

## 🔑 Ortam Değişkenleri

| Değişken | Açıklama | Zorunlu |
|----------|----------|---------|
| `OPENAI_API_KEY` | OpenAI API anahtarı | ✅ AI özellikleri için |
| `JWT_SECRET` | En az 32 karakter gizli anahtar | ✅ |
| `AMADEUS_CLIENT_ID` | Amadeus API kimliği | ⚡ Uçuş araması için |
| `AMADEUS_CLIENT_SECRET` | Amadeus API sırrı | ⚡ |
| `DB_HOST` | PostgreSQL sunucu adresi | Prod için |
| `DB_NAME` | Veritabanı adı | Prod için |
| `DB_USER` | DB kullanıcısı | Prod için |
| `DB_PASSWORD` | DB şifresi | Prod için |

---

## 🏥 Sağlık Kontrolü

```bash
curl http://localhost:8080/api/health
# {"status":"UP","service":"bonvo-backend","timestamp":"..."}
```
