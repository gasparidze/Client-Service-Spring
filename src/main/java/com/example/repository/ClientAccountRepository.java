package com.example.repository;

import com.example.entity.ClientAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository для работы со счетом клиента
 */
public interface ClientAccountRepository extends JpaRepository<ClientAccount, Integer> {
    /**
     * метод, который ищет счет по id клиента
     * @param id - идентификатор клиента
     * @return - Optional<ClientAccount> - счет клиента
     */
    Optional<ClientAccount> findClientAccountByClientId(Integer id);
}
