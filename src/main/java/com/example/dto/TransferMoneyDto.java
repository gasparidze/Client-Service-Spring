package com.example.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldNameConstants;

/**
 * Dto-класс, использующийся для передачи данных о переводе средств со счета аутентифицированного клиента
 * на счет другого клиента
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Builder
public class TransferMoneyDto {
    @NotNull(message = "id must not be null")
    Integer recipientId;

    @Positive(message = "amount must be positive")
    @NotNull(message = "amount must not be null")
    Float amount;
}
