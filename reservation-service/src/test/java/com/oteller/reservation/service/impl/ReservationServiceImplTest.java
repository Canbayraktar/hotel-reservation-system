package com.oteller.reservation.service.impl;

import com.oteller.reservation.event.ReservationCreatedEvent;
import com.oteller.reservation.kafka.ReservationEventProducer;
import com.oteller.reservation.model.Reservation;
import com.oteller.reservation.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceImplTest {

    private ReservationRepository repository;
    private ReservationEventProducer eventProducer;
    private ReservationServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(ReservationRepository.class);
        eventProducer = mock(ReservationEventProducer.class);
        service = new ReservationServiceImpl(repository, eventProducer);
    }

    @Test
    void hasConflict_shouldReturnTrueIfConflictingReservationExists() {
        Reservation res = new Reservation();
        res.setRoomId(1L);
        res.setCheckInDate(LocalDate.of(2025, 5, 1));
        res.setCheckOutDate(LocalDate.of(2025, 5, 3));

        when(repository.findConflictingReservations(1L, res.getCheckInDate(), res.getCheckOutDate()))
            .thenReturn(Collections.singletonList(new Reservation()));

        assertTrue(service.hasConflict(res));
    }
    
    @Test
    void hasConflict_shouldReturnFalseIfNoConflictExists() {
        Reservation res = new Reservation();
        res.setRoomId(1L);
        res.setCheckInDate(LocalDate.of(2025, 5, 1));
        res.setCheckOutDate(LocalDate.of(2025, 5, 3));

        when(repository.findConflictingReservations(1L, res.getCheckInDate(), res.getCheckOutDate()))
            .thenReturn(Collections.emptyList());

        assertFalse(service.hasConflict(res));
    }
    
    @Test
    void save_shouldSendEventAfterSaving() {
        Reservation res = new Reservation();
        res.setId(1L);
        res.setHotelId(2L);
        res.setRoomId(3L);
        res.setGuestName("Test Guest");
        res.setCheckInDate(LocalDate.of(2025, 5, 1));
        res.setCheckOutDate(LocalDate.of(2025, 5, 3));
        
        when(repository.save(res)).thenReturn(res);
        
        service.save(res);
        
        verify(eventProducer, times(1)).sendReservationCreatedEvent(any(ReservationCreatedEvent.class));
    }
    
    @Test
    void createWithLockCheck_shouldSaveReservationWhenNoConflicts() {
        Reservation res = new Reservation();
        res.setRoomId(1L);
        res.setGuestName("Test Guest");
        res.setCheckInDate(LocalDate.of(2025, 5, 1));
        res.setCheckOutDate(LocalDate.of(2025, 5, 3));
        
        when(repository.findConflictingReservationsForUpdate(eq(1L), any(), any()))
            .thenReturn(Collections.emptyList());
        when(repository.save(res)).thenReturn(res);
        
        Reservation result = service.createWithLockCheck(res);
        
        assertNotNull(result);
        verify(eventProducer, times(1)).sendReservationCreatedEvent(any(ReservationCreatedEvent.class));
    }
    
    @Test
    void createWithLockCheck_shouldThrowExceptionWhenConflictsExist() {
        Reservation res = new Reservation();
        res.setRoomId(1L);
        res.setGuestName("Test Guest");
        res.setCheckInDate(LocalDate.of(2025, 5, 1));
        res.setCheckOutDate(LocalDate.of(2025, 5, 3));
        
        when(repository.findConflictingReservationsForUpdate(eq(1L), any(), any()))
            .thenReturn(Collections.singletonList(new Reservation()));
        
        assertThrows(IllegalStateException.class, () -> service.createWithLockCheck(res));
        verify(eventProducer, never()).sendReservationCreatedEvent(any());
    }
} 