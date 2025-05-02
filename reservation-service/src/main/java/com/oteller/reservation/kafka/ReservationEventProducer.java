package com.oteller.reservation.kafka;

import com.oteller.reservation.event.ReservationCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ReservationEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ReservationEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendReservationCreatedEvent(ReservationCreatedEvent event) {
        kafkaTemplate.send("reservation-created", event);
    }
} 