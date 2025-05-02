# Otel Rezervasyon Sistemi - Postman Koleksiyonu

Bu dokümantasyon, Otel Rezervasyon Sistemi API'lerini test etmek için hazırlanmış Postman koleksiyonunun nasıl kullanılacağını açıklamaktadır.

## Gereksinimler

- [Postman](https://www.postman.com/downloads/) uygulaması
- Çalışan bir Otel Rezervasyon Sistemi mikro servis ortamı

## Kurulum Adımları

1. Postman uygulamasını açın
2. **Collections** sekmesinde **Import** butonuna tıklayın
3. `otel-reservation-test.postman_collection.json` dosyasını seçerek import edin
4. **Environments** sekmesinde **Import** butonuna tıklayın
5. `otel-reservation-environment.postman_environment.json` dosyasını seçerek import edin
6. Sağ üst köşedeki ortam seçiciden "Otel Rezervasyon Sistemi - Test Ortamı" seçeneğini seçin

## Kullanım Klavuzu

Koleksiyon, API'leri mantıksal bir düzende test etmek için tasarlanmıştır. Aşağıdaki sırayı takip ederek sistemin tüm özelliklerini test edebilirsiniz:

### 1. Kullanıcı Oluşturma ve Giriş

1. `Auth Service > Register User` isteğini gönderin
   - Bu işlem yeni bir kullanıcı kaydı oluşturacaktır
2. `Auth Service > Login User` isteğini gönderin
   - Bu istek otomatik olarak dönen JWT token'ı ortam değişkenine kaydedecektir

### 2. Otel ve Oda İşlemleri

1. `Hotel Service > Create Hotel` isteğini gönderin
   - Bu istek otomatik olarak oluşturulan otel ID'sini ortam değişkenine kaydedecektir
2. `Hotel Service > Get All Hotels` isteğini göndererek oluşturulan oteli listede görebilirsiniz
3. `Hotel Service > Get Hotel By ID` isteğini göndererek belirli bir oteli ID'sine göre sorgulayabilirsiniz
4. `Hotel Service > Create Room` isteğini gönderin
   - Bu istek otomatik olarak oluşturulan oda ID'sini ortam değişkenine kaydedecektir
5. `Hotel Service > Get Rooms By Hotel ID` isteğini göndererek bir otele ait tüm odaları listeleyebilirsiniz
6. `Hotel Service > Get Room By ID` isteğini göndererek belirli bir odayı ID'sine göre sorgulayabilirsiniz

### 3. Rezervasyon İşlemleri

1. `Reservation Service > Create Reservation` isteğini gönderin
   - Bu istek otomatik olarak oluşturulan rezervasyon ID'sini ortam değişkenine kaydedecektir
2. `Reservation Service > Get All Reservations` isteğini göndererek tüm rezervasyonları listeleyebilirsiniz
3. `Reservation Service > Get Reservation By ID` isteğini göndererek belirli bir rezervasyonu ID'sine göre sorgulayabilirsiniz
4. `Reservation Service > Get Reservations By Room ID` isteğini göndererek bir odaya ait tüm rezervasyonları listeleyebilirsiniz

## Ortam Değişkenleri

Koleksiyon, aşağıdaki ortam değişkenlerini kullanmaktadır:

- `baseUrl`: API'nin temel URL'i (varsayılan: http://localhost:8080)
- `accessToken`: Giriş işleminden sonra otomatik olarak ayarlanan JWT token
- `hotelId`: Otel oluşturma işleminden sonra otomatik olarak ayarlanan otel ID'si
- `roomId`: Oda oluşturma işleminden sonra otomatik olarak ayarlanan oda ID'si
- `reservationId`: Rezervasyon oluşturma işleminden sonra otomatik olarak ayarlanan rezervasyon ID'si

## Notlar

- API Gateway 8080 portunda çalışacak şekilde yapılandırılmıştır. Eğer farklı bir port kullanıyorsanız, ortam değişkenlerindeki `baseUrl` değerini güncellemeniz gerekir.
- Test koleksiyonu, mikroservislerin doğru bir şekilde yapılandırıldığını ve çalıştığını varsaymaktadır.
- Koleksiyon, testlerin başarılı olması için gereken tüm adımları otomatik olarak gerçekleştirir (örneğin, token ayarlama, ID'leri kaydetme vb.). 