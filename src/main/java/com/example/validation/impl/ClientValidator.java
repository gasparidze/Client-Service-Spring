package com.example.validation.impl;

import com.example.dto.ClientCreateEditDto;
import com.example.service.ClientService;
import com.example.validation.ClientExist;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

/**
 * Класс, валидирующий аннотацию @ClientExist
 */
@RequiredArgsConstructor
public class ClientValidator implements ConstraintValidator<ClientExist, ClientCreateEditDto> {
    private final ClientService clientService;

    /**
     * метод, производящий валидацию переданного dto-объекта
     * @param clientCreateEditDto - dto-объекта, содержащий информацию о клиенте
     * @param context - контекст
     * @return boolean - прошел валидацию или нет
     */
    @Override
    public boolean isValid(ClientCreateEditDto clientCreateEditDto, ConstraintValidatorContext context) {
        return !clientService.doesClientExist(clientCreateEditDto);
    }
}
