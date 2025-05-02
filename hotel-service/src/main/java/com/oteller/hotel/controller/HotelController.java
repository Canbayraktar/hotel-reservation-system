package com.oteller.hotel.controller;

import com.oteller.hotel.dto.HotelDto;
import com.oteller.hotel.mapper.HotelMapper;
import com.oteller.hotel.model.Hotel;
import com.oteller.hotel.service.HotelService;
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
@RequestMapping("/api/hotels")
@Tag(name = "Otel Yönetimi", description = "Otel kayıt ve yönetim işlemleri")
public class HotelController {

    private final HotelService hotelService;
    private final HotelMapper hotelMapper;

    public HotelController(HotelService hotelService, HotelMapper hotelMapper) {
        this.hotelService = hotelService;
        this.hotelMapper = hotelMapper;
    }

    @Operation(summary = "Yeni otel oluşturma", description = "Yeni bir otel kaydı oluşturur")
    @ApiResponse(responseCode = "201", description = "Otel başarıyla oluşturuldu")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<HotelDto> create(@Valid @RequestBody HotelDto dto) {
        Hotel saved = hotelService.save(hotelMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(hotelMapper.toDto(saved));
    }

    @Operation(summary = "Tüm otelleri listele", description = "Sistemdeki tüm otelleri listeler")
    @GetMapping
    public List<HotelDto> findAll() {
        return hotelMapper.toDtoList(hotelService.findAll());
    }

    @Operation(summary = "Otel detayı görüntüleme", description = "ID'ye göre otel bilgilerini getirir")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Otel bulundu"),
        @ApiResponse(responseCode = "404", description = "Otel bulunamadı")
    })
    @GetMapping("/{id}")
    public ResponseEntity<HotelDto> findById(@PathVariable Long id) {
        return hotelService.findById(id)
                .map(hotel -> ResponseEntity.ok(hotelMapper.toDto(hotel)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Otel bilgilerini güncelleme", description = "Var olan otelin bilgilerini günceller")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Otel başarıyla güncellendi"),
        @ApiResponse(responseCode = "404", description = "Otel bulunamadı")
    })
    @PutMapping("/{id}")
    public ResponseEntity<HotelDto> update(@PathVariable Long id, @Valid @RequestBody HotelDto dto) {
        return hotelService.findById(id)
                .map(existing -> {
                    Hotel updated = hotelMapper.updateEntityFromDto(dto, existing);
                    return ResponseEntity.ok(hotelMapper.toDto(hotelService.save(updated)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        hotelService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
} 