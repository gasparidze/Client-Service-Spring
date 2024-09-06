package com.example.http.controller;

import com.example.dto.TransferMoneyDto;
import com.example.integration.IntegrationTestBase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Тестовый класс, проверяющий ClientRestController
 */
@AutoConfigureMockMvc
@RequiredArgsConstructor
class ClientRestControllerTest extends IntegrationTestBase {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    /**
     * метод, тестирующий трансфер денежных средств
     * @throws Exception - exception
     */
    @Test
    void transferMoney() throws Exception {
        TransferMoneyDto transferMoneyDto = new TransferMoneyDto(2, 20f);
        String requestBody = objectMapper.writeValueAsString(transferMoneyDto);
        mockMvc.perform(patch("/api/v1/clients/transferring")
                        .with(user("test1@mail.ru"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().is2xxSuccessful());

    }
}