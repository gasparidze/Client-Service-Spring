package com.example.database.repository;

import com.example.database.entity.ClientAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository для работы со счетом клиента
 */
@Repository
public interface ClientAccountRepository extends JpaRepository<ClientAccount, Integer> {
    /**
     * метод, который ищет счет по id клиента
     * @param id - идентификатор клиента
     * @return - Optional<ClientAccount> - счет клиента
     */
    Optional<ClientAccount> findClientAccountByClientId(Integer id);
}
