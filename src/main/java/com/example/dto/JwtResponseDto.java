package com.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

/**
 * Dto-класс, использующийся для передачи данных jwt с уровня сервисов на уровень контроллеров
 */
@Schema(description = "DTO для отображения токена пользователю")
@Value
public class JwtResponseDto {
    @Schema(description = "тип токена")
    String type = "Bearer";

    @Schema(description = "jwt токен")
    String jwt;
}
