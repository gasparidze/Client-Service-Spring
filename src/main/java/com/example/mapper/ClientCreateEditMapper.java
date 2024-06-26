package com.example.mapper;

import com.example.database.entity.Client;
import com.example.dto.ClientCreateEditDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Маппер, сопоставляющий ClientCreateEditDto с Client
 */
@Component
@RequiredArgsConstructor
public class ClientCreateEditMapper implements Mapper<ClientCreateEditDto, Client>{
    private final PasswordEncoder passwordEncoder;

    @Override
    public Client map(ClientCreateEditDto object) {
        return Client.builder()
                .fio(object.getFio())
                .birthDate(object.getBirthDate())
                .login(object.getLogin())
                .password(passwordEncoder.encode(object.getPassword()))
                .phone(object.getPhone())
                .email(object.getEmail())
                .build();
    }
}
