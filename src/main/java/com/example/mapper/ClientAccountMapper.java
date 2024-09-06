package com.example.mapper;

import com.example.entity.ClientAccount;
import com.example.dto.ClientAccountReadDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * маппер для ClientAccount
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ClientAccountMapper {
    ClientAccountReadDto objectToDto(ClientAccount clientAccount);
}
