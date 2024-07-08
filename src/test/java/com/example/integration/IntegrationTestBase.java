package com.example.integration;

import com.example.integration.annotation.IT;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Абстрактный класс, который должен наследовать каждый интеграционный тестовый класс
 */
@IT
@Sql({
        "classpath:sql/data.sql"
})
@WithMockUser(username = "test@mail.ru", password = "test")
public abstract class IntegrationTestBase {
    /**
     * test container
     */
    private static final PostgreSQLContainer<?> container
            = new PostgreSQLContainer<>("postgres:14.1");

    /**
     * метод, отвечающий за запуск test container
     */
    @BeforeAll
    static void runContainer(){
        container.start();
    }

    /**
     * метод динамически устанавливающий url в test container
     * @param registry
     */
    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", container::getJdbcUrl);
    }
}
