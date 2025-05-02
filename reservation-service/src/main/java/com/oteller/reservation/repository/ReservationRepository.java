package com.oteller.reservation.repository;

import com.oteller.reservation.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.time.LocalDate;
import java.util.List;

/**
 * Rezervasyon veri erişim arayüzü.
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    /**
     * Belirli bir odanın tarih aralığında çakışan rezervasyonlarını bulur.
     * 
     * @param roomId oda ID'si
     * @param checkIn giriş tarihi
     * @param checkOut çıkış tarihi
     * @return çakışan rezervasyonların listesi
     */
    @Query("SELECT r FROM Reservation r WHERE r.roomId = :roomId AND " +
           "r.checkInDate < :checkOut AND r.checkOutDate > :checkIn")
    List<Reservation> findConflictingReservations(
            @Param("roomId") Long roomId, 
            @Param("checkIn") LocalDate checkIn, 
            @Param("checkOut") LocalDate checkOut);
            
    /**
     * Belirli bir odanın tarih aralığında çakışan rezervasyonlarını PESSIMISTIC_WRITE kilit modu ile bulur.
     * Bu metot, veritabanı seviyesinde kilit alarak eşzamanlı rezervasyon çakışmalarını önler.
     * 
     * @param roomId oda ID'si
     * @param checkIn giriş tarihi
     * @param checkOut çıkış tarihi
     * @return çakışan rezervasyonların listesi
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM Reservation r WHERE r.roomId = :roomId AND " +
           "r.checkInDate < :checkOut AND r.checkOutDate > :checkIn")
    List<Reservation> findConflictingReservationsForUpdate(
            @Param("roomId") Long roomId, 
            @Param("checkIn") LocalDate checkIn, 
            @Param("checkOut") LocalDate checkOut);
} 