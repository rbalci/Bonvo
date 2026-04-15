# ✈️ Bonvo — Ürün Gereksinim Belgesi (PRD)

> **Versiyon:** 1.0 (Enterprise Architecture)  
> **Durum:** MVP Tanımı

## 1. Ürün Özeti

**Bonvo**, kullanıcının ilgi alanlarını analiz ederek uçuş, konaklama ve kişiselleştirilmiş gezi planını tek bir akışta sunan **Java & React tabanlı** AI seyahat asistanıdır.

## 2. Kullanıcı Hikayeleri (User Stories)

- **US-01:** Kullanıcı olarak, Java backend'in sağladığı tip güvenliği ile bütçeme en uygun uçuşları hatasız görmek istiyorum.
- **US-02:** Kullanıcı olarak, React arayüzünde AI planının akıcı (streaming) bir şekilde oluşmasını izlemek istiyorum.
- **US-03:** Kullanıcı olarak, ilgi alanlarıma (Örn: Sanat galerileri) özel durakları içeren bir program almak istiyorum.

## 3. Fonksiyonel Gereksinimler

- **FR-01 (Profilleme):** Sistem kullanıcıdan hobi ve bütçe verisi alır. Bu veriler Spring AI üzerinden prompt bağlamı olarak işlenir.
- **FR-02 (Uçuş/Otel):** Java RestTemplate/WebClient kullanılarak Amadeus ve Booking API'larından veri çekilir.
- **FR-03 (AI Plan):** Planlar, Server-Sent Events (SSE) ile React tarafına aktarılır.

## 4. Teknik Mimari

### Backend (Java + Spring Boot)

- **Spring AI:** LLM entegrasyonu için.
- **Spring Security:** API uç noktası güvenliği için.
- **Lombok:** Boilerplate kodu azaltmak için.

### Frontend (React + TypeScript)

- **Axios:** REST API iletişimi için.
- **React Query:** Sunucu durum yönetimi için.
- **Tailwind CSS:** Modern ve responsive tasarım için.

## 5. Başarı Metrikleri

- Plan tamamlama oranı: %60+
- AI yanıt süresi (ilk byte): < 3 saniye
- API hata oranı: < %5
