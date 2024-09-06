package com.example.http.controller;

import com.example.dto.JwtRequestDto;
import com.example.dto.JwtResponseDto;
import com.example.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Rest-контроллер, отвечающий за аутентификацию
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "AuthenticationController", description = "Контроллер для аутентификации клиента")
public class AuthRestController {
    private final AuthService authService;

    /**
     * метод, отвечающий за создание jwt токена
     * @param jwtRequestDto - dto объект, содержащий информацию необходимую для создания токена
     * @return JwtResponseDto - dto объект, содержащий информацию о токене
     */
    @Operation(
            summary = "Создание токена авторизации",
            description = "Позволяет создать JWT токен для авторизации в системе"
    )
    @PostMapping("/auth")
    @ResponseStatus(HttpStatus.CREATED)
    public JwtResponseDto createAuthToken(@Valid @RequestBody JwtRequestDto jwtRequestDto){
        return authService.createAuthToken(jwtRequestDto);
    }
}
