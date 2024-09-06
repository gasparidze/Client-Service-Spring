package com.example.dto;

import com.example.validation.ClientExist;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Value;
import java.math.BigDecimal;
import java.time.LocalDate;


/**
 * Dto-класс, использующийся для передачи данных клиента с уровня контроллеров на уровень сервисов
 */
@Schema(description = "DTO для создания и обновления клиента в системе")
@Value
@ClientExist
public class ClientCreateEditDto {
    @Schema(description = "фио")
    @NotBlank(message = "fio must not be null")
    private String fio;

    @Schema(description = "дата рождения")
    @NotNull(message = "birthDate must not be null")
    private LocalDate birthDate;

    @Schema(description = "логин")
    @NotBlank(message = "login must not be empty")
    private String login;

    @Schema(description = "пароль")
    @NotBlank(message = "password must not be empty")
    private String password;

    @Schema(description = "номер телефона")
    @NotBlank(message = "phone must not be empty")
    private String phone;

    @Schema(description = "email")
    @Email(message = "email isn't valid")
    private String email;

    @Schema(description = "баланс для счета клиента")
    @NotNull(message = "balance must not be null")
    @Positive(message = "balance must be positive")
    private BigDecimal balance;
}
