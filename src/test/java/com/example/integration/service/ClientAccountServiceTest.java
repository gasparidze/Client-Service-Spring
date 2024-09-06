package com.example.integration.service;

import com.example.entity.ClientAccount;
import com.example.repository.ClientAccountRepository;
import com.example.dto.TransferMoneyDto;
import com.example.http.exception.ClientAccountException;
import com.example.integration.IntegrationTestBase;
import com.example.service.ClientAccountService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Интеграционный тестовый класс для ClientAccountService
 */
@RequiredArgsConstructor
class ClientAccountServiceTest extends IntegrationTestBase {
    private static final Integer SENDER_CLIENT_ID = 1;
    private static final Integer RECIPIENT_CLIENT_ID = 2;
    private static final Integer RECIPIENT_FAKE_CLIENT_ID = 14;
    private static final String SENDER_LOGIN = "test1@mail.ru";

    private final ClientAccountService clientAccountService;

    private final ClientAccountRepository clientAccountRepository;

    /**
     * метод, тестирующий трансфер денежных средств
     */
    @Test
    void transferMoney() {
        TransferMoneyDto transferMoneyDto
                = new TransferMoneyDto(RECIPIENT_CLIENT_ID, 20f);

        clientAccountService.transferMoney(SENDER_LOGIN, transferMoneyDto);

        ClientAccount senderAccount
                = clientAccountRepository.findClientAccountByClientId(SENDER_CLIENT_ID).get();
        ClientAccount recipientAccount
                = clientAccountRepository.findClientAccountByClientId(RECIPIENT_CLIENT_ID).get();

        assertThat(senderAccount.getBalance()).isEqualTo(new BigDecimal(80));
        assertThat(recipientAccount.getBalance()).isEqualTo(new BigDecimal(120));
    }

    /**
     * параметризованный метод, тестирующий исключительные сценарии при переводе денежных средств
     * @param transferMoneyDto - dto-объект, содержащий информацию необходимую для перевода денежных средств
     * @param expectedMessage - сообщение об ошибке
     */
    @ParameterizedTest
    @MethodSource("getArgumentsForExceptionScenarios")
    void transferMoneyExceptionScenarios(TransferMoneyDto transferMoneyDto, String expectedMessage){
        ClientAccountException exception = assertThrows(
                ClientAccountException.class,
                () -> clientAccountService.transferMoney(SENDER_LOGIN, transferMoneyDto)
        );

        assertThat(exception.getMessage()).isEqualTo(expectedMessage);
    }

    /**
     * метод, предоставляющий данные для параметризованного тестового метода
     * @return Stream<Arguments> - стрим аргументов
     */
    static Stream<Arguments> getArgumentsForExceptionScenarios(){
        return Stream.of(
                Arguments.of(
                        new TransferMoneyDto(RECIPIENT_FAKE_CLIENT_ID, 20f),
                        "Счет у клиента не найден"
                ),
                Arguments.of(
                        new TransferMoneyDto(RECIPIENT_CLIENT_ID, 120f),
                        "На счету недостаточно средств"
                )
        );
    }
}