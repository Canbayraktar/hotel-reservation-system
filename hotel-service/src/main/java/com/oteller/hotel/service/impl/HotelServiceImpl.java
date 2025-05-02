package com.oteller.hotel.service.impl;

import com.oteller.hotel.model.Hotel;
import com.oteller.hotel.repository.HotelRepository;
import com.oteller.hotel.service.HotelService;
import com.oteller.common.service.GenericServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class HotelServiceImpl extends GenericServiceImpl<Hotel, Long> implements HotelService {
    
    private final HotelRepository hotelRepository;
    
    public HotelServiceImpl(HotelRepository repository) {
        super(repository);
        this.hotelRepository = repository;
    }
} 