package com.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldNameConstants;

/**
 * Dto-класс, использующийся для передачи данных о переводе средств со счета аутентифицированного клиента
 * на счет другого клиента
 */
@Schema(description = "DTO для перевода средств на счет клиента-получателя")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Builder
public class TransferMoneyDto {
    @Schema(description = "id получателя")
    @NotNull(message = "id must not be null")
    Integer recipientId;

    @Schema(description = "сумма")
    @Positive(message = "amount must be positive")
    @NotNull(message = "amount must not be null")
    Float amount;
}
