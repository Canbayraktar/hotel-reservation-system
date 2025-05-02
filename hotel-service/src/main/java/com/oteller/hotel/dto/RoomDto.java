package com.oteller.hotel.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RoomDto {
    private Long id;
    private String roomNumber;
    private int capacity;
    private BigDecimal pricePerNight;
    private Long hotelId;
} 