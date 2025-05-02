package com.oteller.reservation.mapper;

import com.oteller.reservation.dto.ReservationDto;
import com.oteller.reservation.model.Reservation;
import com.oteller.common.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * Rezervasyon entity ve DTO dönüşümleri için mapper.
 */
@Mapper(componentModel = "spring")
public interface ReservationMapper extends GenericMapper<ReservationDto, Reservation> {
    
    @Override
    Reservation toEntity(ReservationDto dto);
    
    @Override
    ReservationDto toDto(Reservation entity);
    
    @Override
    Reservation updateEntityFromDto(ReservationDto dto, @MappingTarget Reservation entity);
} 