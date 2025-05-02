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
    
    @Operation(summary = "Yeni oda oluşturma", description = "Yeni bir oda kaydı oluşturur")
    @ApiResponse(responseCode = "201", description = "Oda başarıyla oluşturuldu")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<RoomDto> create(@Valid @RequestBody RoomDto dto) {
        Room saved = roomService.save(roomMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(roomMapper.toDto(saved));
    }
    
    @Operation(summary = "Tüm odaları listele", description = "Sistemdeki tüm odaları listeler")
    @GetMapping
    public List<RoomDto> findAll() {
        return roomMapper.toDtoList(roomService.findAll());
    }
    
    @Operation(summary = "Otele göre oda listele", description = "Belirli bir otele ait odaları listeler")
    @GetMapping("/by-hotel/{hotelId}")
    public List<RoomDto> findByHotelId(@PathVariable Long hotelId) {
        return roomMapper.toDtoList(roomService.findByHotelId(hotelId));
    }
    
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

    @Operation(summary = "Oda silme", description = "ID'si verilen odayı sistemden siler")
    @ApiResponse(responseCode = "204", description = "Oda başarıyla silindi")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roomService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
} 