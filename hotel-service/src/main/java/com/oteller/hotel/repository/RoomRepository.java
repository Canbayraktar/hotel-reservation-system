package com.oteller.hotel.repository;

import com.oteller.hotel.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Oda veri erişim arayüzü.
 */
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    
    /**
     * Belirli bir otele ait odaları bulur.
     *
     * @param hotelId otel ID'si
     * @return otel odalarının listesi
     */
    List<Room> findByHotelId(Long hotelId);
} 