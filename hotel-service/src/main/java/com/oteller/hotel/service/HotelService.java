package com.oteller.hotel.service;

import com.oteller.hotel.model.Hotel;
import com.oteller.common.service.GenericService;

/**
 * Otel servis arayüzü.
 * GenericService'in sağladığı CRUD operasyonlarını kullanır.
 */
public interface HotelService extends GenericService<Hotel, Long> {
} 