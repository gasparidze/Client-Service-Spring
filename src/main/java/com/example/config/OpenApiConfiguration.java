package com.example.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Конфиг для OpenAPI (swagger)
 */
@OpenAPIDefinition(
        info = @Info(
                title = "Client Service API",
                description = "Client Service", version = "1.0.0",
                contact = @Contact(
                        name = "Arthur Gasparyan",
                        email = "gaspar-art@mail.ru",
                        url = "https://github.com/gasparidze"
                )
        )
)
@SecurityScheme(
        name = "JWT",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@Configuration
public class OpenApiConfiguration {
}
