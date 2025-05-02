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

@Service
public class ReservationServiceImpl extends GenericServiceImpl<Reservation, Long> implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationEventProducer eventProducer;

    public ReservationServiceImpl(ReservationRepository repository, ReservationEventProducer eventProducer) {
        super(repository);
        this.reservationRepository = repository;
        this.eventProducer = eventProducer;
    }

    @Override
    public boolean hasConflict(Reservation reservation) {
        // Aynı id'ye sahip olan rezervasyon, kendisi ile çakışmaz
        List<Reservation> conflicts = reservationRepository.findConflictingReservations(
                reservation.getRoomId(),
                reservation.getCheckInDate(),
                reservation.getCheckOutDate()
        );
        
        if (reservation.getId() != null) {
            conflicts = conflicts.stream()
                    .filter(r -> !r.getId().equals(reservation.getId()))
                    .toList();
        }
        
        return !conflicts.isEmpty();
    }
    
    @Override
    @Transactional
    public Reservation createWithLockCheck(Reservation reservation) {
        List<Reservation> conflicts = reservationRepository.findConflictingReservationsForUpdate(
                reservation.getRoomId(),
                reservation.getCheckInDate(),
                reservation.getCheckOutDate()
        );
        
        if (reservation.getId() != null) {
            conflicts = conflicts.stream()
                    .filter(r -> !r.getId().equals(reservation.getId()))
                    .toList();
        }
        
        if (!conflicts.isEmpty()) {
            throw new IllegalStateException("Bu oda için seçilen tarih aralığında başka bir rezervasyon bulunmaktadır");
        }
        
        Reservation saved = reservationRepository.save(reservation);
        
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