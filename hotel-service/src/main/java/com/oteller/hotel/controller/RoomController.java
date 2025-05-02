package com.oteller.hotel.controller;

import com.oteller.hotel.dto.RoomDto;
import com.oteller.hotel.mapper.RoomMapper;
import com.oteller.hotel.model.Room;
import com.oteller.hotel.service.RoomService;
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

/**
 * Oda REST Controller sınıfı.
 * Oda ile ilgili HTTP isteklerini karşılar ve işler.
 */
@RestController
@RequestMapping("/api/rooms")
@Tag(name = "Oda Yönetimi", description = "Oda kayıt ve yönetim işlemleri")
public class RoomController {

    private final RoomService roomService;
    private final RoomMapper roomMapper;

    public RoomController(RoomService roomService, RoomMapper roomMapper) {
        this.roomService = roomService;
        this.roomMapper = roomMapper;
    }

    /**
     * Yeni oda oluşturur.
     *
     * @param dto oda bilgilerini içeren DTO
     * @return oluşturulan oda bilgileri
     */
    @Operation(summary = "Yeni oda oluşturma", description = "Yeni bir oda kaydı oluşturur")
    @ApiResponse(responseCode = "201", description = "Oda başarıyla oluşturuldu")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<RoomDto> create(@Valid @RequestBody RoomDto dto) {
        Room saved = roomService.save(roomMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(roomMapper.toDto(saved));
    }

    /**
     * Tüm odaları getirir.
     *
     * @return odaların listesi
     */
    @Operation(summary = "Tüm odaları listele", description = "Sistemdeki tüm odaları listeler")
    @GetMapping
    public List<RoomDto> findAll() {
        return roomMapper.toDtoList(roomService.findAll());
    }

    /**
     * Belirli bir otele ait odaları getirir.
     *
     * @param hotelId otel ID'si
     * @return otel odalarının listesi
     */
    @Operation(summary = "Otele göre oda listele", description = "Belirli bir otele ait odaları listeler")
    @GetMapping("/by-hotel/{hotelId}")
    public List<RoomDto> findByHotelId(@PathVariable Long hotelId) {
        return roomMapper.toDtoList(roomService.findByHotelId(hotelId));
    }

    /**
     * ID'ye göre oda getirir.
     *
     * @param id oda ID'si
     * @return bulunan oda, yoksa 404
     */
    @Operation(summary = "Oda detayı görüntüleme", description = "ID'ye göre oda bilgilerini getirir")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Oda bulundu"),
        @ApiResponse(responseCode = "404", description = "Oda bulunamadı")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RoomDto> findById(@PathVariable Long id) {
        return roomService.findById(id)
                .map(room -> ResponseEntity.ok(roomMapper.toDto(room)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Oda bilgilerini günceller.
     *
     * @param id oda ID'si
     * @param dto güncellenecek bilgileri içeren DTO
     * @return güncellenen oda bilgileri, oda yoksa 404
     */
    @Operation(summary = "Oda bilgilerini güncelleme", description = "Var olan odanın bilgilerini günceller")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Oda başarıyla güncellendi"),
        @ApiResponse(responseCode = "404", description = "Güncellenecek oda bulunamadı")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<RoomDto> update(@PathVariable Long id, @Valid @RequestBody RoomDto dto) {
        return roomService.findById(id)
                .map(existing -> {
                    Room updated = roomMapper.updateEntityFromDto(dto, existing);
                    return ResponseEntity.ok(roomMapper.toDto(roomService.save(updated)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Odayı siler.
     *
     * @param id silinecek oda ID'si
     * @return HTTP 204 No Content
     */
    @Operation(summary = "Oda silme", description = "ID'si verilen odayı sistemden siler")
    @ApiResponse(responseCode = "204", description = "Oda başarıyla silindi")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roomService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
} 