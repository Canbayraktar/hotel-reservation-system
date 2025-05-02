package com.oteller.reservation.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationDto {
    private Long id;
    
    @NotNull(message = "Otel ID boş olamaz")
    private Long hotelId;
    
    @NotNull(message = "Oda ID boş olamaz")
    private Long roomId;
    
    @NotBlank(message = "Misafir adı boş olamaz")
    private String guestName;
    
    @NotNull(message = "Giriş tarihi boş olamaz")
    @Future(message = "Giriş tarihi gelecekte olmalıdır")
    private LocalDate checkInDate;
    
    @NotNull(message = "Çıkış tarihi boş olamaz")
    @Future(message = "Çıkış tarihi gelecekte olmalıdır")
    private LocalDate checkOutDate;
} 