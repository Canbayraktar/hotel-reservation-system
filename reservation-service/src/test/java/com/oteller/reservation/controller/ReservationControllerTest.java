package com.oteller.reservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oteller.reservation.dto.ReservationDto;
import com.oteller.reservation.mapper.ReservationMapper;
import com.oteller.reservation.model.Reservation;
import com.oteller.reservation.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;
    
    @MockBean
    private ReservationMapper reservationMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create_shouldReturn201WithNoConflict() throws Exception {
        ReservationDto dto = new ReservationDto();
        dto.setHotelId(1L);
        dto.setRoomId(1L);
        dto.setGuestName("John Doe");
        dto.setCheckInDate(LocalDate.now());
        dto.setCheckOutDate(LocalDate.now().plusDays(2));
        
        Reservation reservation = new Reservation();
        reservation.setHotelId(1L);
        reservation.setRoomId(1L);
        reservation.setGuestName("John Doe");
        reservation.setCheckInDate(LocalDate.now());
        reservation.setCheckOutDate(LocalDate.now().plusDays(2));
        
        when(reservationMapper.toEntity(any(ReservationDto.class))).thenReturn(reservation);
        when(reservationService.createWithLockCheck(any(Reservation.class))).thenReturn(reservation);
        when(reservationMapper.toDto(any(Reservation.class))).thenReturn(dto);

        mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }
    
    @Test
    void create_shouldReturn409WhenConflictExists() throws Exception {
        ReservationDto dto = new ReservationDto();
        dto.setHotelId(1L);
        dto.setRoomId(1L);
        dto.setGuestName("John Doe");
        dto.setCheckInDate(LocalDate.now());
        dto.setCheckOutDate(LocalDate.now().plusDays(2));
        
        Reservation reservation = new Reservation();
        reservation.setHotelId(1L);
        reservation.setRoomId(1L);
        reservation.setGuestName("John Doe");
        reservation.setCheckInDate(LocalDate.now());
        reservation.setCheckOutDate(LocalDate.now().plusDays(2));
        
        when(reservationMapper.toEntity(any(ReservationDto.class))).thenReturn(reservation);
        when(reservationService.createWithLockCheck(any(Reservation.class)))
            .thenThrow(new IllegalStateException("Bu oda için seçilen tarih aralığında başka bir rezervasyon bulunmaktadır"));

        mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }
    
    @Test
    void update_shouldReturn200WhenNoConflict() throws Exception {
        Long id = 1L;
        ReservationDto dto = new ReservationDto();
        dto.setId(id);
        dto.setHotelId(1L);
        dto.setRoomId(1L);
        dto.setGuestName("John Doe");
        dto.setCheckInDate(LocalDate.now());
        dto.setCheckOutDate(LocalDate.now().plusDays(2));
        
        Reservation existing = new Reservation();
        existing.setId(id);
        
        Reservation updated = new Reservation();
        updated.setId(id);
        updated.setHotelId(1L);
        updated.setRoomId(1L);
        updated.setGuestName("John Doe");
        updated.setCheckInDate(LocalDate.now());
        updated.setCheckOutDate(LocalDate.now().plusDays(2));
        
        when(reservationService.findById(id)).thenReturn(java.util.Optional.of(existing));
        when(reservationMapper.updateEntityFromDto(any(), any())).thenReturn(updated);
        when(reservationService.createWithLockCheck(any())).thenReturn(updated);
        when(reservationMapper.toDto(any())).thenReturn(dto);
        
        mockMvc.perform(put("/api/reservations/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }
} 