package com.example.service;

import com.example.entity.Client;
import com.example.entity.ClientAccount;
import com.example.repository.ClientAccountRepository;
import com.example.dto.*;
import com.example.http.exception.ClientAccountException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Сервис по работе со счетом клиента
 */
@Service
@Slf4j
@Transactional(readOnly = true)
public class ClientAccountService {
    public static final String ACCOUNT_IS_NOT_FOUND = "Счет у клиента не найден";
    private static final String LOG_PATTERN = "clientId - {}, current balance - {}";
    private static final BigDecimal balanceRaiseLimit = new BigDecimal(207);
    private static final BigDecimal coefficient = new BigDecimal(1.05);
    private static final BigDecimal hundred = new BigDecimal(100);
    private final ClientAccountRepository repository;
    private final ClientService clientService;
    private Map<Integer, BigDecimal> clientAccIdToLimitBalanceMap;

    public ClientAccountService(ClientAccountRepository repository,
                                ClientService clientService) {
        this.repository = repository;
        this.clientService = clientService;
        clientAccIdToLimitBalanceMap = new HashMap<>();
        initClientAccounts();
    }

    /**
     * метод, инициализирующий мапу, где ключом является id счета клиента,
     * а значением - максимальный баланс счета, больше которого увеличение
     * баланса невозможно (т.е. 207% от начального депозита)
     */
    private void initClientAccounts() {
        List<ClientAccount> clientAccounts = repository.findAll();
        clientAccounts.stream().forEach(account -> {
                    clientAccIdToLimitBalanceMap.put(account.getId(), account.getBalance()
                            .multiply(balanceRaiseLimit)
                            .divide(hundred));
                    log.info(LOG_PATTERN, account.getClient().getId(),
                            account.getBalance().setScale(2, RoundingMode.HALF_UP));
                }
        );
    }

    /**
     * метод, увеличиваюший баланс каждого клиента на 5% раз в минуту
     */
    @Transactional
    @Scheduled(fixedRateString = "${scheduler.interval}", initialDelayString = "${scheduler.interval}")
    @Async
    public void accrueInterestToClients() {
        List<ClientAccount> clientAccounts = repository.findAll();
        if (!clientAccounts.isEmpty()) {
            clientAccounts.forEach(account -> {
                if (clientAccIdToLimitBalanceMap.containsKey(account.getId())) {
                    BigDecimal newBalance = account.getBalance().multiply(coefficient);
                    if (clientAccIdToLimitBalanceMap.get(account.getId()).compareTo(newBalance) == 1) {
                        account.setBalance(newBalance);
                        log.info(LOG_PATTERN, account.getClient().getId(),
                                account.getBalance().setScale(2, RoundingMode.HALF_UP));
                    }
                } else {
                    BigDecimal maxBalance = account.getBalance().multiply(balanceRaiseLimit).divide(hundred);
                    account.setBalance(account.getBalance().multiply(coefficient));
                    clientAccIdToLimitBalanceMap.put(account.getId(), maxBalance);
                    log.info(LOG_PATTERN, account.getClient().getId(),
                            account.getBalance().setScale(2, RoundingMode.HALF_UP));
                }
            });
        }
    }

    /**
     * метод, осуществляющий перевод денежных средств с одного счета на другой
     *
     * @param senderLogin      - логин клиента-отправителя денежных средств
     * @param transferMoneyDto - dto-объект, содержащий информацию необходимую для перевода денежных средств
     */
    @Transactional
    public void transferMoney(String senderLogin, TransferMoneyDto transferMoneyDto) {
        Client senderClient = clientService.getClientByLogin(senderLogin);

        ClientAccount senderClientAccount = repository
                .findClientAccountByClientId(senderClient.getId())
                .orElseThrow(() -> new ClientAccountException(ACCOUNT_IS_NOT_FOUND));

        ClientAccount recipientClientAccount = repository
                .findClientAccountByClientId(transferMoneyDto.getRecipientId())
                .orElseThrow(() -> new ClientAccountException(ACCOUNT_IS_NOT_FOUND));

        BigDecimal amountAsBigDecimal = new BigDecimal(transferMoneyDto.getAmount());
        BigDecimal senderBalance = senderClientAccount.getBalance();
        BigDecimal recipientBalance = recipientClientAccount.getBalance();

        if (senderBalance.compareTo(amountAsBigDecimal) >= 0) {
            senderClientAccount.getAccountLock().writeLock().lock();
            recipientClientAccount.getAccountLock().writeLock().lock();

            senderClientAccount.setBalance(senderBalance.subtract(amountAsBigDecimal));
            senderClientAccount.getAccountLock().writeLock().unlock();

            recipientClientAccount.setBalance(recipientBalance.add(amountAsBigDecimal));
            recipientClientAccount.getAccountLock().writeLock().unlock();
        } else {
            throw new ClientAccountException("На счету недостаточно средств");
        }
    }
}