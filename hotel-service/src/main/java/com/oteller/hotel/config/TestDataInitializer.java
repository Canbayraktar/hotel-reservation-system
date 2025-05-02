package com.oteller.hotel.config;

import com.oteller.hotel.model.Hotel;
import com.oteller.hotel.model.Room;
import com.oteller.hotel.repository.HotelRepository;
import com.oteller.hotel.repository.RoomRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Test verileri yükleyici sınıfı.
 * Uygulama başladığında örnek verileri veritabanına ekler.
 */
@Configuration
public class TestDataInitializer {

    @Bean
    public CommandLineRunner initData(
            HotelRepository hotelRepository, 
            RoomRepository roomRepository) {
        return args -> {
            if (hotelRepository.count() == 0) {
                // Örnek otel verileri
                Hotel hotel1 = new Hotel();
                hotel1.setName("Grand Hotel");
                hotel1.setAddress("İstanbul, Taksim");
                hotel1.setStarRating(5);
                hotelRepository.save(hotel1);

                Hotel hotel2 = new Hotel();
                hotel2.setName("Seaside Resort");
                hotel2.setAddress("Antalya, Konyaaltı");
                hotel2.setStarRating(4);
                hotelRepository.save(hotel2);

                Hotel hotel3 = new Hotel();
                hotel3.setName("Mountain View Lodge");
                hotel3.setAddress("Bursa, Uludağ");
                hotel3.setStarRating(3);
                hotelRepository.save(hotel3);
                
                // Örnek oda verileri
                Room room1 = new Room();
                room1.setRoomNumber("101");
                room1.setCapacity(2);
                room1.setPricePerNight(new BigDecimal("1500.00"));
                room1.setHotel(hotel1);
                roomRepository.save(room1);
                
                Room room2 = new Room();
                room2.setRoomNumber("102");
                room2.setCapacity(4);
                room2.setPricePerNight(new BigDecimal("2500.00"));
                room2.setHotel(hotel1);
                roomRepository.save(room2);
                
                Room room3 = new Room();
                room3.setRoomNumber("201");
                room3.setCapacity(2);
                room3.setPricePerNight(new BigDecimal("1200.00"));
                room3.setHotel(hotel2);
                roomRepository.save(room3);
                
                Room room4 = new Room();
                room4.setRoomNumber("301");
                room4.setCapacity(3);
                room4.setPricePerNight(new BigDecimal("900.00"));
                room4.setHotel(hotel3);
                roomRepository.save(room4);
            }
        };
    }
} 