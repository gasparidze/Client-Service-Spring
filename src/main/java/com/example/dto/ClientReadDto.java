package com.example.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

/**
 * Dto-класс, использующийся для передачи данных клиента с уровня сервисов на уровень контроллеров
 */
@Data
@Builder
public class ClientReadDto {
    private Integer id;

    private String fio;

    private LocalDate birthDate;

    private String login;

    private String password;

    private List<String> phones;

    private List<String> emails;

    private ClientAccountReadDto clientAccountReadDto;
}
