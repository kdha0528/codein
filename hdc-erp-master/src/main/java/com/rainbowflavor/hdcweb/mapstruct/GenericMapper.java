package com.rainbowflavor.hdcweb.mapstruct;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * @param <T> is DTO
 * @param <S> is ENTITY
 */

public interface GenericMapper <T,S>{
    T toDto(S s);
    S toEntity(T t);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(T dto, @MappingTarget S entity);
}
