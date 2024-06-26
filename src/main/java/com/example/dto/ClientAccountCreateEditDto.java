package com.example.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

/**
 * Dto-класс, использующийся для передачи данных счета клиента с уровня контроллеров на уровень сервисов
 */
@Value
@Builder
public class ClientAccountCreateEditDto {
    private BigDecimal balance;
    private Integer clientId;
}
