package com.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

/**
 * Dto-класс, использующийся для передачи данных клиента с уровня сервисов на уровень контроллеров
 */
@Schema(description = "DTO для вывода клиента пользователю")
@Data
@Builder
public class ClientReadDto {
    @Schema(description = "id")
    private Integer id;

    @Schema(description = "фио")
    private String fio;

    @Schema(description = "дата рождения")
    private LocalDate birthDate;

    @Schema(description = "логин")
    private String login;

    @Schema(description = "пароль")
    private String password;

    @Schema(description = "список номеров телефона")
    private List<String> phones;

    @Schema(description = "список emails")
    private List<String> emails;

    @Schema(description = "dto счета клиента")
    private ClientAccountReadDto clientAccountReadDto;
}
