package com.oteller.hotel.service;

import com.oteller.hotel.model.Room;
import com.oteller.common.service.GenericService;

import java.util.List;

public interface RoomService extends GenericService<Room, Long> {

    List<Room> findByHotelId(Long hotelId);
} 