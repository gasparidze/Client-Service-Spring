package com.example.dto;

import lombok.Builder;
import lombok.Value;
import java.math.BigDecimal;

/**
 * Dto-класс, использующийся для передачи данных счета клиента с уровня сервисов на уровень контроллеров
 */
@Builder
@Value
public class ClientAccountReadDto {
    private Integer id;
    private BigDecimal balance;
}
