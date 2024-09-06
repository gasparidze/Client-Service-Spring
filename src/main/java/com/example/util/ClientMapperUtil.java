package com.example.util;

import com.example.repository.ClientAccountRepository;
import com.example.dto.ClientAccountReadDto;
import com.example.mapper.ClientAccountMapper;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * util-класс для работы с ClientMapper
 */
@Named("ClientMapperUtil")
@Component
@RequiredArgsConstructor
public class ClientMapperUtil {
    private final PasswordEncoder passwordEncoder;
    private final ClientAccountRepository clientAccountRepository;
    private final ClientAccountMapper clientAccountMapper;

    @Named("encodePassword")
    public String encodePassword(String password){
        return passwordEncoder.encode(password);
    }

    @Named("getClientAccountReadDto")
    public ClientAccountReadDto getClientAccountReadDto(Integer clientId){
        return clientAccountRepository.findClientAccountByClientId(clientId)
                .map(clientAccountMapper::objectToDto)
                .orElseThrow();
    }
}
