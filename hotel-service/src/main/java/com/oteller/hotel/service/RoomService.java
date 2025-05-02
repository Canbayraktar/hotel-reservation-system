package com.oteller.hotel.service;

import com.oteller.hotel.model.Room;
import com.oteller.common.service.GenericService;

import java.util.List;

/**
 * Oda servis arayüzü.
 * GenericService'in sağladığı CRUD operasyonlarını kullanır.
 */
public interface RoomService extends GenericService<Room, Long> {
    
    /**
     * Belirli bir otele ait odaları bulur.
     *
     * @param hotelId otel ID'si
     * @return otel odalarının listesi
     */
    List<Room> findByHotelId(Long hotelId);
} 