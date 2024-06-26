package com.example.mapper;

import com.example.database.entity.Client;
import com.example.database.repository.ClientAccountRepository;
import com.example.dto.ClientAccountReadDto;
import com.example.dto.ClientReadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Optional;

/**
 * Маппер, сопоставляющий Client с ClientReadDto
 */
@Component
@RequiredArgsConstructor
public class ClientReadMapper implements Mapper<Client, ClientReadDto> {
    private final ClientAccountRepository clientAccountRepository;
    private final ClientAccountReadMapper clientAccountReadMapper;

    @Override
    public ClientReadDto map(Client client) {
        return ClientReadDto.builder()
                .id(client.getId())
                .fio(client.getFio())
                .birthDate(client.getBirthDate())
                .login(client.getLogin())
                .password(client.getPassword())
                .emails(client.getEmails())
                .phones(client.getPhones())
                .clientAccountReadDto(getClientAccountDto(client.getId()))
                .build();
    }

    private ClientAccountReadDto getClientAccountDto(Integer clientId) {
        return Optional.ofNullable(clientId)
                .flatMap(clientAccountRepository::findClientAccountByClientId)
                .map(clientAccountReadMapper::map)
                .orElse(null);
    }
}
