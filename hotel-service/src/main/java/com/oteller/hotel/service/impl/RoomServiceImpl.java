package com.oteller.hotel.service.impl;

import com.oteller.hotel.model.Room;
import com.oteller.hotel.repository.RoomRepository;
import com.oteller.hotel.service.RoomService;
import com.oteller.common.service.GenericServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Oda servis implementasyonu.
 * GenericServiceImpl'den türer ve RoomService arayüzünü uygular.
 */
@Service
public class RoomServiceImpl extends GenericServiceImpl<Room, Long> implements RoomService {
    
    private final RoomRepository roomRepository;
    
    /**
     * Constructor injection ile bağımlılıkları alır.
     * 
     * @param repository oda repository'si
     */
    public RoomServiceImpl(RoomRepository repository) {
        super(repository);
        this.roomRepository = repository;
    }
    
    /**
     * Belirli bir otele ait odaları bulur.
     *
     * @param hotelId otel ID'si
     * @return otel odalarının listesi
     */
    @Override
    public List<Room> findByHotelId(Long hotelId) {
        return roomRepository.findByHotelId(hotelId);
    }
} 