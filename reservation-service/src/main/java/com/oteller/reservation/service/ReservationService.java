package com.oteller.reservation.service;

import com.oteller.reservation.model.Reservation;
import com.oteller.common.service.GenericService;

public interface ReservationService extends GenericService<Reservation, Long> {
    
    boolean hasConflict(Reservation reservation);
    Reservation createWithLockCheck(Reservation reservation);
} 