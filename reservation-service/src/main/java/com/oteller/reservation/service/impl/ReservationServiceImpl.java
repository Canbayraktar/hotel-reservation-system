package com.oteller.reservation.service.impl;

import com.oteller.reservation.event.ReservationCreatedEvent;
import com.oteller.reservation.kafka.ReservationEventProducer;
import com.oteller.reservation.model.Reservation;
import com.oteller.reservation.repository.ReservationRepository;
import com.oteller.reservation.service.ReservationService;
import com.oteller.common.service.GenericServiceImpl;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;

/**
 * Rezervasyon servis implementasyonu.
 * GenericServiceImpl'den türer ve ReservationService arayüzünü uygular.
 */
@Service
public class ReservationServiceImpl extends GenericServiceImpl<Reservation, Long> implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationEventProducer eventProducer;

    /**
     * Constructor injection ile bağımlılıkları alır.
     *
     * @param repository rezervasyon repository'si
     * @param eventProducer rezervasyon event producer'ı
     */
    public ReservationServiceImpl(ReservationRepository repository, ReservationEventProducer eventProducer) {
        super(repository);
        this.reservationRepository = repository;
        this.eventProducer = eventProducer;
    }

    /**
     * Rezervasyonun çakışma durumunu kontrol eder.
     * Aynı oda için, tarih aralıklarında çakışma olup olmadığını kontrol eder.
     *
     * @param reservation kontrol edilecek rezervasyon
     * @return çakışma varsa true, yoksa false
     */
    @Override
    public boolean hasConflict(Reservation reservation) {
        // Aynı id'ye sahip olan rezervasyon, kendisi ile çakışmaz
        List<Reservation> conflicts = reservationRepository.findConflictingReservations(
                reservation.getRoomId(),
                reservation.getCheckInDate(),
                reservation.getCheckOutDate()
        );
        
        // Eğer bu mevcut bir rezervasyonun güncellemesi ise, kendisi ile çakışmasını göz ardı et
        if (reservation.getId() != null) {
            conflicts = conflicts.stream()
                    .filter(r -> !r.getId().equals(reservation.getId()))
                    .toList();
        }
        
        return !conflicts.isEmpty();
    }
    
    /**
     * Veritabanı kilidi kullanarak (pessimistic locking) çakışma kontrolü yapar ve rezervasyon oluşturur.
     * Bu metot aynı oda için eşzamanlı rezervasyon isteklerinde veri tutarlılığını sağlar.
     *
     * @param reservation oluşturulacak rezervasyon
     * @return kaydedilen rezervasyon
     * @throws IllegalStateException rezervasyon çakışması durumunda
     */
    @Override
    @Transactional
    public Reservation createWithLockCheck(Reservation reservation) {
        List<Reservation> conflicts = reservationRepository.findConflictingReservationsForUpdate(
                reservation.getRoomId(),
                reservation.getCheckInDate(),
                reservation.getCheckOutDate()
        );
        
        // Mevcut bir rezervasyonun güncellemesi ise, kendisi ile çakışmasını göz ardı et
        if (reservation.getId() != null) {
            conflicts = conflicts.stream()
                    .filter(r -> !r.getId().equals(reservation.getId()))
                    .toList();
        }
        
        if (!conflicts.isEmpty()) {
            throw new IllegalStateException("Bu oda için seçilen tarih aralığında başka bir rezervasyon bulunmaktadır");
        }
        
        Reservation saved = reservationRepository.save(reservation);
        
        // Rezervasyon oluştuğunda event gönder
        ReservationCreatedEvent event = new ReservationCreatedEvent();
        event.setReservationId(saved.getId());
        event.setHotelId(saved.getHotelId());
        event.setRoomId(saved.getRoomId());
        event.setGuestName(saved.getGuestName());
        event.setCheckInDate(saved.getCheckInDate());
        event.setCheckOutDate(saved.getCheckOutDate());
        
        eventProducer.sendReservationCreatedEvent(event);
        
        return saved;
    }
    
    @Override
    public Reservation save(Reservation entity) {
        Reservation saved = super.save(entity);
        
        // Rezervasyon oluştuğunda event gönder
        ReservationCreatedEvent event = new ReservationCreatedEvent();
        event.setReservationId(saved.getId());
        event.setHotelId(saved.getHotelId());
        event.setRoomId(saved.getRoomId());
        event.setGuestName(saved.getGuestName());
        event.setCheckInDate(saved.getCheckInDate());
        event.setCheckOutDate(saved.getCheckOutDate());
        
        eventProducer.sendReservationCreatedEvent(event);
        
        return saved;
    }
} 