package com.oteller.hotel.mapper;

import com.oteller.hotel.dto.HotelDto;
import com.oteller.hotel.model.Hotel;
import com.oteller.common.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * Otel entity ve DTO dönüşümleri için mapper.
 */
@Mapper(componentModel = "spring")
public interface HotelMapper extends GenericMapper<HotelDto, Hotel> {
    
    @Override
    Hotel toEntity(HotelDto dto);
    
    @Override
    HotelDto toDto(Hotel entity);
    
    @Override
    Hotel updateEntityFromDto(HotelDto dto, @MappingTarget Hotel entity);
} 