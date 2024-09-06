package com.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import java.math.BigDecimal;

/**
 * Dto-класс, использующийся для передачи данных счета клиента с уровня сервисов на уровень контроллеров
 */
@Schema(description = "DTO для вывода счета клиента пользователю")
@Builder
@Value
public class ClientAccountReadDto {
    @Schema(description = "id счета клиента")
    private Integer id;

    @Schema(description = "баланс")
    private BigDecimal balance;
}
