package com.example.mapper;

import com.example.database.entity.ClientAccount;
import com.example.dto.ClientAccountReadDto;
import org.springframework.stereotype.Component;

/**
 * Маппер, сопоставляющий ClientAccount с ClientAccountReadDto
 */
@Component
public class ClientAccountReadMapper implements Mapper<ClientAccount, ClientAccountReadDto>{
    @Override
    public ClientAccountReadDto map(ClientAccount clientAccount) {
        return ClientAccountReadDto.builder()
                .id(clientAccount.getId())
                .balance(clientAccount.getBalance())
                .build();
    }
}
