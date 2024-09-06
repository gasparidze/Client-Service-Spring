package com.example.service;

import com.example.entity.Client;
import com.example.entity.ClientAccount;
import com.example.repository.ClientAccountRepository;
import com.example.dto.TransferMoneyDto;
import com.example.http.exception.ClientAccountException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

/**
 * Класс для Unit тестирования ClientAccountService
 */
@ExtendWith(MockitoExtension.class)
class ClientAccountServiceTest {
    private static final Integer SENDER_CLIENT_ID = 1;
    private static final Integer RECIPIENT_CLIENT_ID = 2;
    private static final Integer RECIPIENT_FAKE_CLIENT_ID = 14;
    private static final String SENDER_LOGIN = "test1@mail.ru";
    private static BigDecimal balance = new BigDecimal(100);
    private Client client;
    private ClientAccount senderClientAccount;
    @Mock
    private ClientService clientService;
    @Mock
    private ClientAccountRepository clientAccountRepository;
    @InjectMocks
    private ClientAccountService clientAccountService;

    @BeforeEach
    void initData(){
        client = Client.builder().id(SENDER_CLIENT_ID).login(SENDER_LOGIN).build();
        senderClientAccount = ClientAccount.builder().balance(balance).build();

        doReturn(client)
                .when(clientService).getClientByLogin(SENDER_LOGIN);
        doReturn(Optional.of(senderClientAccount))
                .when(clientAccountRepository).findClientAccountByClientId(SENDER_CLIENT_ID);
    }

    /**
     * метод, тестирующий трансфер денежных средств
     */
    @Test
    void transferMoney() {
        ClientAccount recipientClientAccount = ClientAccount.builder().balance(balance).build();
        TransferMoneyDto transferMoneyDto = new TransferMoneyDto(RECIPIENT_CLIENT_ID, 20f);

        doReturn(Optional.of(recipientClientAccount))
                .when(clientAccountRepository).findClientAccountByClientId(RECIPIENT_CLIENT_ID);

        clientAccountService.transferMoney(SENDER_LOGIN, transferMoneyDto);

        assertThat(senderClientAccount.getBalance()).isEqualTo(new BigDecimal(80));
        assertThat(recipientClientAccount.getBalance()).isEqualTo(new BigDecimal(120));
    }

    /**
     * параметризованный метод, тестирующий исключительные сценарии при переводе денежных средств
     * @param transferMoneyDto - dto-объект, содержащий информацию необходимую для перевода денежных средств
     * @param expectedMessage - сообщение об ошибке
     */
    @ParameterizedTest
    @MethodSource("getArgumentsForExceptionScenarios")
    void transferMoneyExceptionScenarios(TransferMoneyDto transferMoneyDto, String expectedMessage){
        if(transferMoneyDto.getRecipientId() != RECIPIENT_FAKE_CLIENT_ID) {
            ClientAccount recipientClientAccount = ClientAccount.builder().balance(balance).build();
            doReturn(Optional.of(recipientClientAccount))
                    .when(clientAccountRepository).findClientAccountByClientId(RECIPIENT_CLIENT_ID);
        }

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