package com.oteller.hotel.mapper;

import com.oteller.hotel.dto.RoomDto;
import com.oteller.hotel.model.Hotel;
import com.oteller.hotel.model.Room;
import com.oteller.common.mapper.BaseMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RoomMapper extends BaseMapper<RoomDto, Room> {

    @Mapping(target = "hotel.id", source = "hotelId")
    Room toEntity(RoomDto dto);

    @Mapping(source = "hotel.id", target = "hotelId")
    RoomDto toDto(Room entity);

    @Mapping(target = "hotel.id", source = "hotelId")
    Room updateEntityFromDto(RoomDto dto, @MappingTarget Room entity);

    @ObjectFactory
    default Room resolve(@TargetType Class<Room> type, RoomDto dto) {
        Room room = new Room();
        room.setHotel(new Hotel());
        room.getHotel().setId(dto.getHotelId());
        return room;
    }
} 