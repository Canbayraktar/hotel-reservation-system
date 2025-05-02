package com.oteller.reservation.service;

import com.oteller.reservation.model.Reservation;
import com.oteller.common.service.GenericService;

/**
 * Rezervasyon servis arayüzü.
 * GenericService'in sağladığı CRUD operasyonlarına ek olarak
 * rezervasyon kontrolü için işlevler sunar.
 */
public interface ReservationService extends GenericService<Reservation, Long> {
    
    /**
     * Rezervasyonun çakışma durumunu kontrol eder.
     * 
     * @param reservation kontrol edilecek rezervasyon
     * @return çakışma varsa true, yoksa false
     */
    boolean hasConflict(Reservation reservation);
    
    /**
     * Veritabanı kilidi kullanarak (pessimistic locking) çakışma kontrolü yapar ve rezervasyon oluşturur.
     * Bu metot aynı oda için eşzamanlı rezervasyon isteklerinde veri tutarlılığını sağlar.
     * 
     * @param reservation oluşturulacak rezervasyon
     * @return kaydedilen rezervasyon veya çakışma varsa exception fırlatır
     * @throws IllegalStateException rezervasyon çakışması durumunda
     */
    Reservation createWithLockCheck(Reservation reservation);
} 