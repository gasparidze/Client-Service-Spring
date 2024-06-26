package com.example.http.handler;

import com.example.dto.ExceptionResponseDto;
import com.example.http.exception.AuthException;
import com.example.http.exception.ClientAccountException;
import com.example.http.exception.ClientException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.*;

/**
 * Обработчик исключений
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.example.http.rest")
public class RestControllerExceptionHandler{

    /**
     * метод, обрабатывающий исключения, выбрасывающиеся валидационными аннотациямм
     * @param ex - exception
     * @return ResponseEntity - ответ
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleException(MethodArgumentNotValidException ex){
        List<String> errors = new ArrayList<>();
        ex.getAllErrors().forEach(el -> errors.add(el.getDefaultMessage()));

        return ResponseEntity.badRequest().body(new ExceptionResponseDto(errors));
    }

    /**
     * метод, обрабатывающий исключения, выбрасывающиеся при:
     * 1) валидации параметров методов контроллеров (ConstraintViolationException)
     * 2) (MethodArgumentTypeMismatchException)
     * 3) процессе взаимодействия с клиентом (ClientException)
     * 4) процессе аутентификации (AuthException)
     * 5) остальных, не клиентских, ошибках
     * @param ex - exception
     * @return ResponseEntity - ответ
     */
    @ExceptionHandler(value = {ConstraintViolationException.class, MethodArgumentTypeMismatchException.class,
            ClientException.class, AuthException.class, RuntimeException.class})
    public ResponseEntity<?> handleException(Exception ex){
        ResponseEntity<ExceptionResponseDto> response;

        if(ex instanceof ConstraintViolationException){
            response = ResponseEntity.badRequest().body(
                    new ExceptionResponseDto(ex.getMessage())
            );
        } else if (ex instanceof MethodArgumentTypeMismatchException) {
            response = ResponseEntity.badRequest().body(
                    new ExceptionResponseDto("Верный шаблон для поля birthDate: dd.mm.yyyy")
            );
        } else if (ex instanceof ClientException || ex instanceof ClientAccountException) {
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ExceptionResponseDto(ex.getMessage())
            );
        } else if (ex instanceof AuthException) {
            response = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ExceptionResponseDto(ex.getMessage())
            );
        } else {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ExceptionResponseDto(ex.getMessage())
            );
        }

        return response;
    }
}
