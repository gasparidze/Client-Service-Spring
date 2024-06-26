package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

/**
 * Dto-класс, использующийся для передачи контактов клиента с уровня контроллеров на уровень сервисов
 */
@Value
public class ClientContactsDto {
    @NotBlank
    String replacedContact;
    @NotBlank
    String newContact;
}
