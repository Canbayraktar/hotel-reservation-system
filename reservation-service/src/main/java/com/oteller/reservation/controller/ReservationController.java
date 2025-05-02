package com.oteller.reservation.controller;

import com.oteller.reservation.dto.ReservationDto;
import com.oteller.reservation.mapper.ReservationMapper;
import com.oteller.reservation.model.Reservation;
import com.oteller.reservation.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@Tag(name = "Rezervasyon Yönetimi", description = "Rezervasyon oluşturma ve yönetim işlemleri")
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;

    public ReservationController(ReservationService reservationService, ReservationMapper reservationMapper) {
        this.reservationService = reservationService;
        this.reservationMapper = reservationMapper;
    }

    @Operation(summary = "Yeni rezervasyon oluşturma", description = "Oda ve tarih bilgilerine göre yeni rezervasyon oluşturur")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Rezervasyon başarıyla oluşturuldu"),
        @ApiResponse(responseCode = "400", description = "Geçersiz tarih bilgileri"),
        @ApiResponse(responseCode = "409", description = "Tarih çakışması mevcut")
    })
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ReservationDto dto) {
        Reservation reservation = reservationMapper.toEntity(dto);
        
        if (reservation.getCheckInDate().isAfter(reservation.getCheckOutDate())) {
            return ResponseEntity.badRequest().body("Giriş tarihi, çıkış tarihinden sonra olamaz");
        }
        
        try {
            Reservation saved = reservationService.createWithLockCheck(reservation);
            return ResponseEntity.status(HttpStatus.CREATED).body(reservationMapper.toDto(saved));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @Operation(summary = "Tüm rezervasyonları listele", description = "Sistemdeki tüm rezervasyonları listeler")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public List<ReservationDto> findAll() {
        return reservationMapper.toDtoList(reservationService.findAll());
    }

    @Operation(summary = "Rezervasyon detayı görüntüleme", description = "ID'ye göre rezervasyon bilgilerini getirir")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rezervasyon bulundu"),
        @ApiResponse(responseCode = "404", description = "Rezervasyon bulunamadı")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    public ResponseEntity<ReservationDto> findById(@PathVariable Long id) {
        return reservationService.findById(id)
                .map(reservation -> ResponseEntity.ok(reservationMapper.toDto(reservation)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Rezervasyon güncelleme", description = "Var olan rezervasyonun tarih veya oda bilgilerini günceller")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rezervasyon başarıyla güncellendi"),
        @ApiResponse(responseCode = "400", description = "Geçersiz tarih bilgileri"),
        @ApiResponse(responseCode = "404", description = "Güncellenecek rezervasyon bulunamadı"),
        @ApiResponse(responseCode = "409", description = "Tarih çakışması mevcut")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody ReservationDto dto) {
        return reservationService.findById(id)
                .map(existing -> {
                    Reservation updated = reservationMapper.updateEntityFromDto(dto, existing);
                    
                    if (updated.getCheckInDate().isAfter(updated.getCheckOutDate())) {
                        return ResponseEntity.badRequest().body("Giriş tarihi, çıkış tarihinden sonra olamaz");
                    }
                    
                    try {
                        Reservation saved = reservationService.createWithLockCheck(updated);
                        return ResponseEntity.ok(reservationMapper.toDto(saved));
                    } catch (IllegalStateException e) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Rezervasyon silme", description = "ID'si verilen rezervasyonu sistemden siler")
    @ApiResponse(responseCode = "204", description = "Rezervasyon başarıyla silindi")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
} 