package com.oteller.notification.event;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationCreatedEvent {
    private Long reservationId;
    private Long hotelId;
    private Long roomId;
    private String guestName;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
} 