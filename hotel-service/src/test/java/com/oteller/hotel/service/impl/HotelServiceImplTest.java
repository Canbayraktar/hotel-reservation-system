package com.oteller.hotel.service.impl;

import com.oteller.hotel.model.Hotel;
import com.oteller.hotel.repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HotelServiceImplTest {

    private HotelRepository repository;
    private HotelServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(HotelRepository.class);
        service = new HotelServiceImpl(repository);
    }

    @Test
    void testFindById_ShouldReturnHotel() {
        Hotel hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Test Hotel");

        when(repository.findById(1L)).thenReturn(Optional.of(hotel));

        Optional<Hotel> result = service.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Hotel", result.get().getName());
    }
} 