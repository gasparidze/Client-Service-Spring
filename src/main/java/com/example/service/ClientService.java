package com.example.service;

import com.example.database.entity.Client;
import com.example.database.repository.ClientRepository;
import com.example.dto.ClientContactsDto;
import com.example.dto.ClientCreateEditDto;
import com.example.dto.ClientReadDto;
import com.example.http.exception.ClientException;
import com.example.mapper.ClientCreateEditMapper;
import com.example.mapper.ClientReadMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientCreateEditMapper clientCreateEditMapper;
    private final ClientReadMapper clientReadMapper;

    /**
     * метод, отвечающий за создание клиента в системе
     * @param clientDto - dto-объект, содержащий информацию о клиенте
     * @return clientReadDto - dto-объект, содержащий информацию о клиенте вместе со сгенерированным id
     */
    @Transactional
    public ClientReadDto createClient(ClientCreateEditDto clientDto){
        return Optional.of(clientDto)
                .map(clientCreateEditMapper::map)
                .map(clientRepository::save)
                .map(clientReadMapper::map)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * метод, проверяющий существование клиента по логину ИЛИ номеру телефона ИЛИ по email
     * @param client - to-объект, содержащий информацию о клиенте
     * @return boolean - true/false - существует ли такой клиент или нет
     */
    public boolean doesClientExist(ClientCreateEditDto client){
        return clientRepository.findClientByLoginOrEmailOrPhone(client.getLogin(), client.getPhone(), client.getEmail());
    }

    /**
     * метод, добавляющий email клиенту
     * @param id - идентификатор клиента
     * @param email - email
     */
    @Transactional
    public void addEmailToClient(Integer id, String email){
        doesClientExist(id);
        clientRepository.updateClientEmails(id, email);
    }

    /**
     * метод, добавляющий номер телефона клиенту
     * @param id - идентификатор клиента
     * @param phone - номер телефона
     */
    @Transactional
    public void addPhoneToClient(Integer id, String phone){
        doesClientExist(id);
        clientRepository.updateClientPhoneNumbers(id, phone);
    }

    /**
     * метод, изменяющий существующий email клиента на новый
     * @param id - идентификатор клиента
     * @param clientContactsDto - dto-объект, содержащий информацию о контакте клиента
     */
    @Transactional
    public void changeClientEmail(Integer id, ClientContactsDto clientContactsDto) {
        doesClientExist(clientRepository.findClientByEmail(clientContactsDto.getReplacedContact()));

        clientRepository.updateClientEmails(
                id,
                clientContactsDto.getReplacedContact(),
                clientContactsDto.getNewContact()
        );
    }

    /**
     * метод, изменяющий существующий номер телефона клиента на новый
     * @param id - идентификатор клиента
     * @param clientContactsDto - dto-объект, содержащий информацию о контакте клиента
     */
    @Transactional
    public void changeClientPhone(Integer id, ClientContactsDto clientContactsDto) {
        doesClientExist(clientRepository.findClientByPhone(clientContactsDto.getReplacedContact()));

        clientRepository.updateClientPhoneNumbers(
                id,
                clientContactsDto.getReplacedContact(),
                clientContactsDto.getNewContact()
        );
    }

    /**
     * метод, удаляющий email клиента
     * @param id - идентификатор клиента
     * @param email - email
     */
    @Transactional
    public void removeClientEmail(Integer id, String email){
        doesClientExist(clientRepository.findClientByEmail(email));

        if(clientRepository.countClientEmails(id) == 1){
            throw new ClientException("Нельзя удалить последний email");
        }
        clientRepository.deleteClientEmail(id, email);
    }

    /**
     * метод, удаляющий номер телефона клиента
     * @param id - идентификатор клиента
     * @param phone - номер телефона
     */
    @Transactional
    public void removeClientPhone(Integer id, String phone){
        doesClientExist(clientRepository.findClientByPhone(phone));

        if(clientRepository.countClientPhoneNumbers(id) == 1){
            throw new ClientException("Нельзя удалить последний номер телефона");
        }
        clientRepository.deleteClientPhone(id, phone);
    }

    /**
     * метод, получающий клиента по email
     * @param email - email
     * @return сlientReadDto - dto-объект, содержащий информацию о клиенте
     */
    public ClientReadDto getClientByEmail(String email) {
        Optional<Client> foundClient = clientRepository.findClientByEmail(email);
        doesClientExist(foundClient);

        return foundClient.map(clientReadMapper::map).get();
    }

    /**
     * метод, получающий клиента по номеру телефона
     * @param phone - номер телефона
     * @return сlientReadDto - dto-объект, содержащий информацию о клиенте
     */
    public ClientReadDto getClientByPhone(String phone) {
        Optional<Client> foundClient = clientRepository.findClientByPhone(phone);
        doesClientExist(foundClient);

        return foundClient.map(clientReadMapper::map).get();
    }

    /**
     * метод, возвращающий клиентов по фио согласно пагинации
     * @param page - номер страницы/количество пропущенных страниц
     * @param size - количество элементов на странице
     * @param fio - фио
     * @return Page<ClientReadDto> - клиенты
     */
    public Page<ClientReadDto> getClientsByFio(int page, int size, String fio) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Client> foundClients = clientRepository.findByFioContainingIgnoreCaseOrderByFio(pageable, fio);
        doClientsExist(foundClients);

        return foundClients.map(clientReadMapper::map);
    }

    /**
     * метод, возвращающий клиентов, рожденных позже указанной даты рождения, согласно пагинации
     * @param page - номер страницы/количество пропущенных страниц
     * @param size - количество элементов на странице
     * @param birthDate - даты рождения
     * @return Page<ClientReadDto> - клиенты
     */
    public Page<ClientReadDto> getClientsByBirthDate(int page, int size, LocalDate birthDate) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Client> foundClients = clientRepository.findByBirthDateAfterOrderByBirthDate(pageable, birthDate);
        doClientsExist(foundClients);

        return foundClients.map(clientReadMapper::map);
    }

    /**
     * метод, получающий клиента по логину
     * @param login - логин
     * @return сlient - объект клиента
     */
    public Client getClientByLogin(String login){
        Optional<Client> client = clientRepository.findClientByLogin(login);
        doesClientExist(client);

        return client.get();
    }

    /**
     * метод, проверяющий, удалось ли найти клиентов или нет
     * @param clients - клиенты
     */
    private void doClientsExist(Page<Client> clients) {
        if(clients.isEmpty()){
            throw new ClientException("Клиенты по данному фильтру не найдены");
        }
    }

    /**
     * метод, проверяющий, найден лм клиент по данному id или нет
     * @param id - идентификатор клиента
     */
    private void doesClientExist(Integer id) {
        if(!clientRepository.findClientById(id).isPresent()){
            throw new ClientException("Клиент по данному id не найден");
        }
    }

    /**
     * метод, проверяющий, есть лм клиент в Optional или нет
     * @param client - объект клиента
     */
    private void doesClientExist(Optional<Client> client) {
        if(!client.isPresent()){
            throw new ClientException("Клиент не найден");
        }
    }
}