package com.oteller.hotel.service.impl;

import com.oteller.hotel.model.Room;
import com.oteller.hotel.repository.RoomRepository;
import com.oteller.hotel.service.RoomService;
import com.oteller.common.service.GenericServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl extends GenericServiceImpl<Room, Long> implements RoomService {
    
    private final RoomRepository roomRepository;
    
    public RoomServiceImpl(RoomRepository repository) {
        super(repository);
        this.roomRepository = repository;
    }
    
    @Override
    public List<Room> findByHotelId(Long hotelId) {
        return roomRepository.findByHotelId(hotelId);
    }
} 