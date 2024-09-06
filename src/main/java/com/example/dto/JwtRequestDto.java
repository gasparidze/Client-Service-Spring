package com.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;

/**
 * Dto-класс, использующийся для передачи данных jwt с уровня контроллеров на уровень сервисов
 */
@Schema(description = "DTO для запроса на создание токена")
@Value
public class JwtRequestDto {
    @Schema(description = "логин пользователя")
    @NotBlank(message = "login must not be empty")
    String login;

    @Schema(description = "пароль пользователя")
    @NotBlank(message = "password must not be empty")
    String password;
}
