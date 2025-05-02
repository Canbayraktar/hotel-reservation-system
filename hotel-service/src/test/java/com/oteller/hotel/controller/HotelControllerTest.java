package com.oteller.hotel.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oteller.hotel.dto.HotelDto;
import com.oteller.hotel.mapper.HotelMapper;
import com.oteller.hotel.model.Hotel;
import com.oteller.hotel.service.HotelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HotelController.class)
class HotelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HotelService hotelService;
    
    @MockBean
    private HotelMapper hotelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createHotel_shouldReturn201() throws Exception {
        HotelDto dto = new HotelDto();
        dto.setName("Test Hotel");
        dto.setAddress("Test Address");
        dto.setStarRating(5);
        
        Hotel hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Test Hotel");
        
        when(hotelMapper.toEntity(any(HotelDto.class))).thenReturn(hotel);
        when(hotelService.save(any(Hotel.class))).thenReturn(hotel);
        when(hotelMapper.toDto(any(Hotel.class))).thenReturn(dto);

        mockMvc.perform(post("/api/hotels")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }
    
    @Test
    void getHotelById_shouldReturn200() throws Exception {
        Hotel hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Test Hotel");
        
        HotelDto dto = new HotelDto();
        dto.setId(1L);
        dto.setName("Test Hotel");
        
        when(hotelService.findById(1L)).thenReturn(java.util.Optional.of(hotel));
        when(hotelMapper.toDto(hotel)).thenReturn(dto);
        
        mockMvc.perform(get("/api/hotels/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Hotel"));
    }
} 