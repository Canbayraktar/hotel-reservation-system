package com.oteller.reservation.config;

import com.oteller.reservation.model.Reservation;
import com.oteller.reservation.repository.ReservationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

/**
 * Test verileri yükleyici sınıfı.
 * Uygulama başladığında örnek verileri veritabanına ekler.
 */
@Configuration
public class TestDataInitializer {

    @Bean
    public CommandLineRunner initData(ReservationRepository reservationRepository) {
        return args -> {
            if (reservationRepository.count() == 0) {
                // Örnek rezervasyon verileri
                Reservation reservation1 = new Reservation();
                reservation1.setHotelId(1L);
                reservation1.setRoomId(101L);
                reservation1.setGuestName("Ali Yılmaz");
                reservation1.setCheckInDate(LocalDate.now().plusDays(5));
                reservation1.setCheckOutDate(LocalDate.now().plusDays(10));
                reservationRepository.save(reservation1);
                
                Reservation reservation2 = new Reservation();
                reservation2.setHotelId(1L);
                reservation2.setRoomId(102L);
                reservation2.setGuestName("Ayşe Kaya");
                reservation2.setCheckInDate(LocalDate.now().plusDays(15));
                reservation2.setCheckOutDate(LocalDate.now().plusDays(20));
                reservationRepository.save(reservation2);
                
                Reservation reservation3 = new Reservation();
                reservation3.setHotelId(2L);
                reservation3.setRoomId(201L);
                reservation3.setGuestName("Mehmet Demir");
                reservation3.setCheckInDate(LocalDate.now().plusDays(3));
                reservation3.setCheckOutDate(LocalDate.now().plusDays(8));
                reservationRepository.save(reservation3);
            }
        };
    }
} 