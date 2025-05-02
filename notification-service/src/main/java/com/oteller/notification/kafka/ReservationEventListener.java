package com.oteller.notification.kafka;

import com.oteller.notification.event.ReservationCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ReservationEventListener {

    private static final Logger logger = LoggerFactory.getLogger(ReservationEventListener.class);

    @KafkaListener(topics = "reservation-created", groupId = "notification-group", containerFactory = "kafkaListenerContainerFactory")
    public void handleReservationCreatedEvent(ReservationCreatedEvent event) {
        logger.info("Received ReservationCreatedEvent: {}", event);
        
        // Bildirim gönderme simülasyonu
        logger.info("Sending notification email to guest: {}", event.getGuestName());
        logger.info("Reservation details: Hotel ID: {}, Room ID: {}, Check-in: {}, Check-out: {}", 
                event.getHotelId(), 
                event.getRoomId(), 
                event.getCheckInDate(), 
                event.getCheckOutDate());
        
        // Gerçek uygulamada burada e-posta gönderme servisi çağrılabilir. Şuan fazla vaktim yok.
    }
} 