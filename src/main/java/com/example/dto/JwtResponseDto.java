package com.example.dto;

import lombok.Value;

/**
 * Dto-класс, использующийся для передачи данных jwt с уровня сервисов на уровень контроллеров
 */
@Value
public class JwtResponseDto {
    String type = "Bearer";
    String jwt;
}
