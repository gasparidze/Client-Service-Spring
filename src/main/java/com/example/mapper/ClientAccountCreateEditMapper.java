package com.example.mapper;

import com.example.database.entity.Client;
import com.example.database.entity.ClientAccount;
import com.example.database.repository.ClientRepository;
import com.example.dto.ClientAccountCreateEditDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Optional;

/**
 * Маппер, сопоставляющий ClientAccountCreateEditDto с ClientAccount
 */
@Component
@RequiredArgsConstructor
public class ClientAccountCreateEditMapper implements Mapper<ClientAccountCreateEditDto, ClientAccount> {
    private final ClientRepository clientRepository;
    @Override
    public ClientAccount map(ClientAccountCreateEditDto object) {
        return ClientAccount.builder()
                .balance(object.getBalance())
                .client(getClient(object.getClientId()))
                .build();
    }

    private Client getClient(Integer clientId) {
        return Optional.ofNullable(clientId)
                .flatMap(clientRepository::findById)
                .orElse(null);
    }
}
