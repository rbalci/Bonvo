# Bonvo - Implementation Plan

Bu belge, Bonvo seyahat asistanı projesinin teknik uygulama adımlarını içerir. Proje Java Spring Boot (Backend) ve React TypeScript (Frontend) mimarisi üzerine inşa edilecektir.

## 1. Hazırlık ve Altyapı (Hafta 2)

- [ ] **Backend Kurulumu:** Java 17+, Spring Boot 3.x, Spring Web, Spring AI ve Lombok bağımlılıklarının kurulması.
- [ ] **Frontend Kurulumu:** Vite, React, TypeScript ve Tailwind CSS kurulumlarının yapılması.
- [ ] **API Dokümantasyonu:** Swagger (SpringDoc) entegrasyonu.
- [ ] **Proje Yapılandırması:** Backend ve Frontend servislerinin birbiriyle konuşabilmesi için CORS ayarlarının yapılması.

## 2. Kullanıcı Profili ve Veri Modeli

- [ ] **Backend:** Kullanıcı ilgi alanları ve bütçe tercihlerini tutacak DTO'ların oluşturulması.
- [ ] **Frontend:** İlgi alanı seçim ekranı (Hobi kartları, bütçe slider) tasarımı.
- [ ] **Entegrasyon:** Profil verilerinin `/api/profile` üzerinden backend'e gönderilmesi.

## 3. Harici API Entegrasyonları

- [ ] **Amadeus Client:** Uçuş verilerini çekmek için gerekli servis katmanının Java tarafında yazılması.
- [ ] **Booking/RapidAPI Client:** Konaklama verileri için entegrasyonun tamamlanması.
- [ ] **Mock Veri Mekanizması:** API limitlerine takılmamak için fallback (yedek) veri sisteminin kurulması.

## 4. AI Engine ve Streaming (SSE)

- [ ] **Spring AI Entegrasyonu:** API anahtarlarının yapılandırılması.
- [ ] **Prompt Engineering:** Kullanıcı profiline göre kişiselleştirilmiş tatil planı üreten prompt taslaklarının hazırlanması.
- [ ] **SSE Implementation:** `/api/plan` endpoint'inin stream olarak (Server-Sent Events) React'e veri aktarması.

## 5. Dil Rehberi Modülü

- [ ] **Statik Veri:** Temel diller için kategori bazlı ifadelerin eklenmesi.
- [ ] **AI Phrase Generator:** Kullanıcı hobilerine özel ek kelimelerin üretilmesi.

## 6. Final Dokunuşlar ve Deployment

- [ ] **UI/UX:** Tailwind ile tasarımın son haline getirilmesi.
- [ ] **Deployment:** Backend'in Railway/Render, Frontend'in Vercel üzerinden yayına alınması.
