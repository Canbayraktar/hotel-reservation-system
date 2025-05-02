package com.oteller.auth.mapper;

import com.oteller.auth.dto.UserDto;
import com.oteller.auth.model.User;
import com.oteller.common.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * Kullanıcı entity ve DTO dönüşümleri için mapper.
 */
@Mapper(componentModel = "spring")
public interface UserMapper extends GenericMapper<UserDto, User> {
    
    @Override
    User toEntity(UserDto dto);
    
    @Override
    UserDto toDto(User entity);
    
    @Override
    User updateEntityFromDto(UserDto dto, @MappingTarget User entity);
} 