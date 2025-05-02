# 🏨 Otel Rezervasyon Sistemi (Mikroservis + Event-Driven + JWT Auth)

Bu sistem otel, oda ve rezervasyon işlemlerini yöneten, Kafka ile event-driven mimariye sahip mikroservis tabanlı bir uygulamadır.  
JWT tabanlı kimlik doğrulama, merkezi gateway, PostgreSQL veritabanı ve Docker Compose desteği ile uçtan uca tasarlanmıştır.

## 🔧 Teknolojiler

| Teknoloji | Açıklama |
|-----------|----------|
| Java 21 | Ana programlama dili |
| Spring Boot 3.4.5 | Mikroservis framework'ü |
| Spring Web | REST API |
| Spring Data JPA | Veritabanı erişimi |
| Spring Security | Güvenlik katmanı |
| Spring Cloud Gateway | API Gateway |
| PostgreSQL | İlişkisel veritabanı |
| Apache Kafka | Event messaging |
| MapStruct | DTO-Entity dönüşümü |
| Lombok | Kod azaltma |
| JUnit 5 + Mockito | Test framework'ü |
| JaCoCo | Kod kapsama analizi |
| Swagger/OpenAPI | API dokümantasyon |
| Docker & Docker Compose | Konteynerizasyon |

## 📁 Mikroservisler ve Portlar

| Servis | Açıklama | Port |
|--------|----------|------|
| API Gateway | JWT kontrol & routing | 8080 |
| Auth Service | Register / Login | 8081 |
| Hotel Service | Hotel & Room CRUD | 8082 |
| Reservation Service | Rezervasyon & Kafka Pub | 8083 |
| Notification Service | Kafka Consumer & Logging | 8084 |
| PostgreSQL | Veritabanı | 5432 |
| Kafka | Messaging | 9092 |

## ⚙️ Kurulum (Docker-Based)

```bash
# Proje JAR dosyalarını oluştur
mvn clean package -DskipTests

# Docker ortamını başlat
cd docker
docker-compose up --build
```

## 🔐 Kimlik Doğrulama

- Kullanıcılar `/auth/register` → `/auth/login` üzerinden JWT token alır
- Gateway, `/auth/**` dışındaki tüm isteklerde `Authorization: Bearer <token>` ister
- `X-Authenticated-User` header'ı ile kullanıcı bilgisi servis katmanlarına aktarılır

## 📨 Kafka Event Akışı

```
ReservationService → Kafka ("reservation-created" topic) → NotificationService
```

## 🔎 API Docs ve Araçlar

**Swagger:**
- http://localhost:8081/swagger-ui.html (auth)
- http://localhost:8082/swagger-ui.html (hotel)
- http://localhost:8083/swagger-ui.html (reservation)

## ✅ Test Kapsama

```bash
mvn verify
open target/site/jacoco/index.html
```

- Unit ve integration testler tüm servislerde tanımlıdır
- Coverage oranı HTML raporu ile analiz edilir

## 🧪 Locking Mekanizması

- Aynı odanın aynı tarih aralığında birden fazla rezerve edilmesi `PESSIMISTIC_WRITE` lock ile engellenir
- `@Transactional` servis metodu + `@Lock` query kullanılmıştır

## 📂 Proje Yapısı

```
.
├── auth-service          # Kimlik doğrulama servisi
├── hotel-service         # Otel ve oda yönetimi 
├── reservation-service   # Rezervasyon servisi (Lock ve Kafka Producer)
├── notification-service  # Bildirim servisi (Kafka Consumer)
├── gateway-service       # API Gateway (JWT ve Routing)
├── common-lib            # Ortak sınıflar
└── docker                # Docker Compose dosyaları
``` 