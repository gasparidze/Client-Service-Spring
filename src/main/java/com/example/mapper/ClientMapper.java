package com.example.mapper;

import com.example.entity.Client;
import com.example.dto.ClientCreateEditDto;
import com.example.dto.ClientReadDto;
import com.example.util.ClientMapperUtil;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * маппер для Client
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {ClientMapperUtil.class, ClientAccountMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ClientMapper {
    @Mapping(target = "password", qualifiedByName = {"ClientMapperUtil", "encodePassword"})
    Client dtoToObject(ClientCreateEditDto clientDto);

    @Mapping(target = "clientAccountReadDto",
            qualifiedByName = {"ClientMapperUtil", "getClientAccountReadDto"}, source = "id")
    ClientReadDto objectToDto(Client client);
}
