package com.oteller.hotel.service.impl;

import com.oteller.hotel.model.Hotel;
import com.oteller.hotel.repository.HotelRepository;
import com.oteller.hotel.service.HotelService;
import com.oteller.common.service.GenericServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Otel servis implementasyonu.
 * GenericServiceImpl'den türer ve HotelService arayüzünü uygular.
 */
@Service
public class HotelServiceImpl extends GenericServiceImpl<Hotel, Long> implements HotelService {
    
    private final HotelRepository hotelRepository;
    
    /**
     * Constructor injection ile bağımlılıkları alır.
     * 
     * @param repository otel repository'si
     */
    public HotelServiceImpl(HotelRepository repository) {
        super(repository);
        this.hotelRepository = repository;
    }
} 