package com.example.dto;

import com.example.validation.ClientExist;
import jakarta.validation.constraints.*;
import lombok.Value;
import java.math.BigDecimal;
import java.time.LocalDate;


/**
 * Dto-класс, использующийся для передачи данных клиента с уровня контроллеров на уровень сервисов
 */
@Value
@ClientExist
public class ClientCreateEditDto {
    @NotBlank(message = "fio must not be null")
    private String fio;

    @NotNull(message = "birthDate must not be null")
    private LocalDate birthDate;

    @NotBlank(message = "login must not be empty")
    private String login;

    @NotBlank(message = "password must not be empty")
    private String password;

    @NotBlank(message = "phone must not be empty")
    private String phone;

    @Email(message = "email isn't valid")
    private String email;

    @NotNull(message = "balance must not be null")
    @Positive(message = "balance must be positive")
    private BigDecimal balance;
}
