package com.example.service;

import com.example.entity.Client;
import com.example.entity.ClientAccount;
import com.example.repository.ClientRepository;
import com.example.dto.*;
import com.example.http.exception.ClientException;
import com.example.mapper.ClientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Сервис по работе с клиентом
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClientService {
    private static final String CLIENT_NOT_FOUND = "Клиент не найден ";
    private static final String NF_BY_ID = CLIENT_NOT_FOUND + "по данному id";
    private static final String NF_BY_ID_AND_CONTACT = CLIENT_NOT_FOUND + "по данному id и контакту";

    private final ClientRepository repository;
    private final ClientMapper mapper;

    /**
     * метод, отвечающий за создание клиента и связанного счета в системе
     *
     * @param clientDto - dto-объект, содержащий информацию о клиенте
     * @return clientReadDto - dto-объект, содержащий информацию о клиенте вместе со сгенерированным id
     */
    @Transactional
    public ClientReadDto createClient(ClientCreateEditDto clientDto) {
        ClientAccount clientAccount = ClientAccount.builder()
                .balance(clientDto.getBalance())
                .build();

        return Optional.of(clientDto)
                .map(mapper::dtoToObject)
                .map((client) -> {
                    clientAccount.setClient(client);
                    return client;
                })
                .map(repository::save)
                .map(mapper::objectToDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * метод, проверяющий существование клиента по логину ИЛИ номеру телефона ИЛИ по email
     *
     * @param client - to-объект, содержащий информацию о клиенте
     * @return boolean - true/false - существует ли такой клиент или нет
     */
    public boolean doesClientExist(ClientCreateEditDto client) {
        return repository.findClientByLoginOrEmailOrPhone(client.getLogin(), client.getPhone(), client.getEmail());
    }

    /**
     * метод, добавляющий email клиенту
     *
     * @param id    - идентификатор клиента
     * @param email - email
     */
    @Transactional
    public void addEmailToClient(Integer id, String email) {
        doesClientExist(id);
        repository.updateClientEmails(id, email);
    }

    /**
     * метод, добавляющий номер телефона клиенту
     *
     * @param id    - идентификатор клиента
     * @param phone - номер телефона
     */
    @Transactional
    public void addPhoneToClient(Integer id, String phone) {
        doesClientExist(id);
        repository.updateClientPhoneNumbers(id, phone);
    }

    /**
     * метод, изменяющий существующий email клиента на новый
     *
     * @param id                - идентификатор клиента
     * @param clientContactsDto - dto-объект, содержащий информацию о контакте клиента
     */
    @Transactional
    public void changeClientEmail(Integer id, ClientContactsDto clientContactsDto) {
        checkClientByIdAndEmail(id, clientContactsDto.getReplacedContact());

        repository.updateClientEmails(
                id,
                clientContactsDto.getReplacedContact(),
                clientContactsDto.getNewContact()
        );
    }


    /**
     * метод, изменяющий существующий номер телефона клиента на новый
     *
     * @param id                - идентификатор клиента
     * @param clientContactsDto - dto-объект, содержащий информацию о контакте клиента
     */
    @Transactional
    public void changeClientPhone(Integer id, ClientContactsDto clientContactsDto) {
        checkClientByIdAndPhone(id, clientContactsDto.getReplacedContact());

        repository.updateClientPhoneNumbers(
                id,
                clientContactsDto.getReplacedContact(),
                clientContactsDto.getNewContact()
        );
    }

    /**
     * метод, удаляющий email клиента
     *
     * @param id    - идентификатор клиента
     * @param email - email
     */
    @Transactional
    public void removeClientEmail(Integer id, String email) {
        checkClientByIdAndEmail(id, email);

        if (repository.countClientEmails(id) == 1) {
            throw new ClientException("Нельзя удалить последний email");
        }
        repository.deleteClientEmail(id, email);
    }

    /**
     * метод, удаляющий номер телефона клиента
     *
     * @param id    - идентификатор клиента
     * @param phone - номер телефона
     */
    @Transactional
    public void removeClientPhone(Integer id, String phone) {
        checkClientByIdAndPhone(id, phone);

        if (repository.countClientPhoneNumbers(id) == 1) {
            throw new ClientException("Нельзя удалить последний номер телефона");
        }
        repository.deleteClientPhone(id, phone);
    }

    /**
     * метод, возвращающий клиентов, рожденных позже указанной даты рождения, согласно пагинации
     *
     * @param page      - номер страницы/количество пропущенных страниц
     * @param size      - количество элементов на странице
     * @param birthDate - даты рождения
     * @return Page<ClientReadDto> - клиенты
     */
    public Slice<ClientReadDto> getClientsByBirthDate(int page, int size, LocalDate birthDate) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<Client> foundClients = repository.findByBirthDateAfterOrderByBirthDate(pageable, birthDate);
        doClientsExist(foundClients);

        return foundClients.map(mapper::objectToDto);
    }

    /**
     * метод, получающий клиента по номеру телефона
     *
     * @param phone - номер телефона
     * @return сlientReadDto - dto-объект, содержащий информацию о клиенте
     */
    public Slice<ClientReadDto> getClientsByPhone(int page, int size, String phone) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<Client> foundClients = repository.findClientsByPhone(pageable, phone);
        doClientsExist(foundClients);

        return foundClients.map(mapper::objectToDto);
    }

    /**
     * метод, возвращающий клиентов по фио согласно пагинации
     *
     * @param page - номер страницы/количество пропущенных страниц
     * @param size - количество элементов на странице
     * @param fio  - фио
     * @return Page<ClientReadDto> - клиенты
     */
    public Slice<ClientReadDto> getClientsByFio(int page, int size, String fio) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<Client> foundClients = repository.findByFioContainingIgnoreCaseOrderByFio(pageable, fio);
        doClientsExist(foundClients);

        return foundClients.map(mapper::objectToDto);
    }

    /**
     * метод, получающий клиента по email
     *
     * @param email - email
     * @return сlientReadDto - dto-объект, содержащий информацию о клиенте
     */
    public Slice<ClientReadDto> getClientsByEmail(int page, int size, String email) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<Client> foundClients = repository.findClientsByEmail(pageable, email);
        doClientsExist(foundClients);

        return foundClients.map(mapper::objectToDto);
    }

    /**
     * метод, получающий клиента по логину
     *
     * @param login - логин
     * @return сlient - объект клиента
     */
    public Client getClientByLogin(String login) {
        return repository.findClientByLogin(login)
                .orElseThrow(() -> new ClientException(CLIENT_NOT_FOUND));
    }

    /**
     * метод, проверяющий, удалось ли найти клиентов или нет
     *
     * @param clients - клиенты
     */
    private void doClientsExist(Slice<Client> clients) {
        if (clients.isEmpty()) {
            throw new ClientException("Клиенты по данному фильтру не найдены");
        }
    }

    /**
     * метод, проверяющий, найден лм клиент по данному id или нет
     *
     * @param id - идентификатор клиента
     */
    private void doesClientExist(Integer id) {
        if (!repository.findClientById(id).isPresent()) {
            throw new ClientException(NF_BY_ID);
        }
    }

    /**
     * метод, проверяющий, есть ли клиент по id и email
     *
     * @param id    - id
     * @param email - email
     */
    private void checkClientByIdAndEmail(Integer id, String email) {
        if (!repository.findClientByIdAndEmail(id, email).isPresent()) {
            throw new ClientException(NF_BY_ID_AND_CONTACT);
        }
    }

    /**
     * метод, проверяющий, есть ли клиент по id и номеру телефона
     *
     * @param id    - id
     * @param phone - номер телефона
     */
    private void checkClientByIdAndPhone(Integer id, String phone) {
        if (!repository.findClientByIdAndPhone(id, phone).isPresent()) {
            throw new ClientException(NF_BY_ID_AND_CONTACT);
        }
    }
}