package com.oteller.common.mapper;

import org.mapstruct.MappingTarget;

public interface GenericMapper<D, E> extends BaseMapper<D, E> {
    E updateEntityFromDto(D dto, @MappingTarget E entity);
}