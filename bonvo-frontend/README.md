# ✈️ Bonvo — AI Travel Assistant Frontend

React + TypeScript + Vite ile geliştirilmiş Bonvo frontend uygulaması.

## Teknolojiler

- **React 18** + **TypeScript**
- **React Router v6** — sayfa yönetimi
- **React Query (TanStack)** — sunucu durumu yönetimi
- **Axios** — API istekleri
- **CSS Modules** — bileşen bazlı stil yönetimi
- **Vite** — geliştirme sunucusu ve build aracı

## Kurulum

```bash
# Bağımlılıkları yükle
npm install

# Geliştirme sunucusunu başlat
npm run dev

# Production build
npm run build
```

## Proje Yapısı

```
src/
├── pages/
│   ├── LoginPage.tsx        # Giriş ekranı
│   ├── HomePage.tsx         # Ana sayfa — gezi listesi
│   ├── NewTripPage.tsx      # Yeni gezi oluştur
│   ├── VocabPage.tsx        # Kelime öğrenme
│   └── ProfilePage.tsx      # Kullanıcı profili
├── components/
│   └── AppLayout.tsx        # Ortak header + alt navigasyon
├── styles/
│   └── globals.css          # CSS değişkenleri + reset
├── AppContext.tsx            # Global state (Context API)
├── types.ts                  # TypeScript tipleri
├── data.ts                   # Mock veriler + sabit listeler
├── App.tsx                   # Router yapısı
└── main.tsx                  # Giriş noktası
```

## Sayfalar

| Sayfa | Route | Açıklama |
|-------|-------|----------|
| Login | `/` | Kullanıcı girişi |
| Ana Sayfa | `/home` | Gezi listesi + hızlı erişim |
| Yeni Gezi | `/new-trip` | Destinasyon, tarih, tür, bütçe, ilgi alanları |
| Kelimeler | `/vocab` | Öğrenilen / öğrenilecek / zor kelimeler |
| Profil | `/profile` | Kullanıcı bilgileri + çıkış |

## Backend Entegrasyonu

`src/data.ts` içindeki mock veriler gerçek API'ya bağlanacak şekilde güncellenebilir.

### Örnek API bağlantısı (Axios + React Query)

```typescript
// src/hooks/useTrips.ts
import { useQuery } from '@tanstack/react-query'
import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
})

export function useTrips() {
  return useQuery({
    queryKey: ['trips'],
    queryFn: () => api.get('/trips').then(r => r.data),
  })
}
```

## Tasarım Sistemi

CSS değişkenleri `src/styles/globals.css` içinde tanımlanmıştır:

- `--orange` / `--orange-light` / `--orange-pale` — Ana renk paleti
- `--dark` / `--mid` / `--light` — Metin ve arka plan
- `--font-head: 'Syne'` — Başlık fontu
- `--font-body: 'DM Sans'` — Gövde metni
