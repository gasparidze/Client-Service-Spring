package com.example.repository;

import com.example.entity.Client;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Repository для работы со клиентом
 */
public interface ClientRepository extends JpaRepository<Client, Integer> {
    /**
     * метод, возвращающий клиента по его id
     * @param id - идентификатор клиента
     * @return Optional<Client> - объект Client
     */
    Optional<Client> findClientById(Integer id);

    /**
     * метод, проверяющий, есть ли клиент по таким параметрам как логин, номер телефона и email
     * @param login - логин
     * @param phone - номер телефона
     * @param email - email
     * @return boolean - найдет ли такой клиент или нет
     */
    @Query(nativeQuery = true, value = """
                    SELECT EXISTS(SELECT * FROM client_service.client,
                    unnest(phone_numbers) AS pn, unnest(emails) AS em
                    WHERE login = :login OR pn = :phone OR em = :email);
                    """)
    boolean findClientByLoginOrEmailOrPhone(String login, String phone, String email);

    /**
     * метод, возвращающий клиента по его id и email
     * @param id - id клиента
     * @param email - email
     * @return Optional<Client> - объект Client
     */
    @Query(nativeQuery = true,
            value = """
                    SELECT * FROM client_service.client, unnest(emails) AS em
                    WHERE id = :id
                    AND em = :email
                    """)
    Optional<Client> findClientByIdAndEmail(Integer id, String email);

    /**
     * метод, возвращающий клиента по id и номеру телефона
     * @param id - id клиента
     * @param phone - номер телефона
     * @return Optional<Client> - объект Client
     */
    @Query(nativeQuery = true, value = """
                    SELECT * FROM client_service.client, unnest(phone_numbers) AS pn
                    WHERE id = :id
                    AND pn = :phone
                    """)
    Optional<Client> findClientByIdAndPhone(Integer id, String phone);

    /**
     * метод, добавляющий новый email клиенту по его id
     * @param id - идентификатор клиента
     * @param email - email
     */
    @Modifying
    @Query(nativeQuery = true, value = """
            UPDATE client_service.client
            SET emails = array_append(emails, :email)
            WHERE id = :id
            """)
    void updateClientEmails(Integer id, String email);

    /**
     * метод, изменяющий существующий email на новый у клиента по его id
     * @param id - идентификатор клиента
     * @param replacedEmail - email, который изменяем
     * @param newEmail - новый email
     */
    @Modifying
    @Query(nativeQuery = true, value = """
            UPDATE client_service.client
            SET emails = array_replace(emails, :replacedEmail, :newEmail)
            WHERE id = :id
            """)
    void updateClientEmails(Integer id, String replacedEmail, String newEmail);

    /**
     * метод, добавляющий новый номер телефона клиенту по его id
     * @param id - идентификатор клиента
     * @param phone - номер телефона
     */
    @Modifying
    @Query(nativeQuery = true, value = """
            UPDATE client_service.client
            SET phone_numbers = array_append(phone_numbers, :phone)
            WHERE id = :id
            """)
    void updateClientPhoneNumbers(Integer id, String phone);

    /**
     * метод, изменяющий существующий номер телефона на новый у клиента по его id
     * @param id - идентификатор клиента
     * @param replacedPhone - номер телефона, который изменяем
     * @param newPhone - новый номер телефона
     */
    @Modifying
    @Query(nativeQuery = true, value = """
            UPDATE client_service.client
            SET phone_numbers = array_replace(phone_numbers, :replacedPhone, :newPhone)
            WHERE id = :id
            """)
    void updateClientPhoneNumbers(Integer id, String replacedPhone, String newPhone);

    /**
     * метод, подсчитывающий количество email'ов у клиента по его id
     * @param id - идентификатор клиента
     * @return int - количество email'ов у клиента
     */
    @Query(nativeQuery = true,
            value = """
            SELECT cardinality(emails)
            FROM client_service.client
            WHERE id = :id
            """)
    int countClientEmails(Integer id);

    /**
     * метод, подсчитывающий количество номеров телефона у клиента по его id
     * @param id - идентификатор клиента
     * @return int - количество номеров телефона у клиента
     */
    @Query(nativeQuery = true,
                value = """
            SELECT cardinality(phone_numbers)
            FROM client_service.client
            WHERE id = :id
            """)
    int countClientPhoneNumbers(Integer id);

    /**
     * метод, удаляющий email у клиента по его id
     * @param id - идентификатор клиента
     * @param email - email
     */
    @Modifying
    @Query(nativeQuery = true, value = """
            UPDATE client_service.client
            SET emails = array_remove(emails, :email)
            WHERE id = :id
            """)
    void deleteClientEmail(Integer id, String email);

    /**
     * метод, удаляющий номер телефона у клиента по его id
     * @param id - идентификатор клиента
     * @param phone - номер телефона
     */
    @Modifying
    @Query(nativeQuery = true, value = """
            UPDATE client_service.client 
            SET phone_numbers = array_remove(phone_numbers, :phone)
            WHERE id = :id
            """)
    void deleteClientPhone(Integer id, String phone);

    /**
     * метод, возвращающий клиента по его login
     * @param login - login
     * @return Optional<Client> - объект Client
     */
    Optional<Client> findClientByLogin(String login);

    /**
     * метод, возвращающий клиентов по дате рождения позже указанной и сортирующий по нему
     * @param pageable - объект Pageable
     * @param birthDate - дата рождения
     * @return Page<Client> - клиенты
     */
    Slice<Client> findByBirthDateAfterOrderByBirthDate(Pageable pageable, LocalDate birthDate);

    /**
     * метод, возвращающий клиента по номеру телефона
     * @param phone - номер телефона
     * @return Optional<Client> - объект Client
     */
    @Query(nativeQuery = true, value = """
                    SELECT * FROM client_service.client, unnest(phone_numbers) AS pn
                    WHERE pn = :phone
                    """)
    Slice<Client> findClientsByPhone(Pageable pageable, String phone);

    /**
     * метод, возвращающий клиентов по схожим фио (не учитывая регистр) и сортирующий по нему
     * @param pageable - объект Pageable
     * @param fio - фио клиента
     * @return Page<Client> - клиенты
     */
    Slice<Client> findByFioContainingIgnoreCaseOrderByFio(Pageable pageable, String fio);

    /**
     * метод, возвращающий клиента по его email
     * @param email - email
     * @return Optional<Client> - объект Client
     */
    @Query(nativeQuery = true,
            value = """
                    SELECT * FROM client_service.client, unnest(emails) AS em
                    WHERE em = :email
                    """)
    Slice<Client> findClientsByEmail(Pageable pageable, String email);
}
