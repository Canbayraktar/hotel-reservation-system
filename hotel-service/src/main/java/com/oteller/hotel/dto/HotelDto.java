package com.oteller.hotel.dto;

import lombok.Data;

@Data
public class HotelDto {
    private Long id;
    private String name;
    private String address;
    private int starRating;
} 