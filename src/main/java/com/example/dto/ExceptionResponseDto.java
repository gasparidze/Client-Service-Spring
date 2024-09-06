package com.example.dto;

import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

/**
 * Dto-класс, использующийся для передачи сообщений об ошибках с уровня сервисов на уровень контроллеров
 */
@Getter
public class ExceptionResponseDto {
    private List<String> messages;

    public ExceptionResponseDto(List<String> messages) {
        this.messages = messages;
    }

    public ExceptionResponseDto(String message) {
        this.messages = new ArrayList<>();
        this.messages.add(message);
    }
}
