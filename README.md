# PaymentPanel

PaymentPanel, banka hesapları, ödeme yöntemleri ve para transferlerini yönetmek için tasarlanmış modern bir ödeme sistemi platformudur. Proje, kullanıcıların para yatırma işlemleri gerçekleştirebildiği bir kullanıcı arayüzü ve yöneticilerin işlemleri takip edip onaylayabildiği bir admin paneli içerir.

## Özellikler

- **Kullanıcı Arayüzü:** 
  - Ödeme yöntemleri listesi
  - Ödeme formu (banka bilgisi, IBAN, tutar, açıklama)
  - İşlem geçmişi takibi

- **Admin Paneli:**
  - İşlem onaylama/reddetme
  - Banka hesapları yönetimi
  - Ödeme yöntemleri yönetimi
  - Detaylı raporlar ve istatistikler
  - Kullanıcı yönetimi

## Teknolojiler

### Backend
- Java 17
- Spring Boot 3.1
- PostgreSQL
- Spring Data JPA
- RESTful API

### Frontend (Admin Panel)
- React 18
- Material-UI (MUI)
- Recharts (İstatistik grafikleri)
- Formik & Yup (Form validasyon)
- React Router
- Axios

### Frontend (Kullanıcı Arayüzü)
- React 18
- Tailwind CSS
- React Router
- Axios

## Kurulum

### Gereksinimler
- Java 17+
- Node.js 16+
- PostgreSQL 14+

### Backend Kurulumu
```bash
# PostgreSQL veritabanı oluştur
createdb payment_panel

# Repository klonla
git clone https://github.com/kullaniciadi/payment-panel.git
cd payment-panel/backend

# Spring Boot uygulamasını başlat
./mvnw spring-boot:run