package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

/**
 * Dto-класс, использующийся для передачи данных jwt с уровня контроллеров на уровень сервисов
 */
@Value
public class JwtRequestDto {
    @NotBlank(message = "login must not be empty")
    String login;

    @NotBlank(message = "password must not be empty")
    String password;
}
