package com.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;

/**
 * Dto-класс, использующийся для передачи контактов клиента с уровня контроллеров на уровень сервисов
 */
@Schema(description = "DTO для передачи контактов клиента")
@Value
public class ClientContactsDto {
    @Schema(description = "старый контакт")
    @NotBlank
    String replacedContact;

    @Schema(description = "новый контакт")
    @NotBlank
    String newContact;
}
