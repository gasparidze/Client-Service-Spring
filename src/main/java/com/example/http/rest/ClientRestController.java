package com.example.http.rest;

import com.example.dto.*;
import com.example.service.ClientAccountService;
import com.example.service.ClientService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.time.LocalDate;

/**
 * Rest-контроллер, отвечающий за взаимодействие с системой банковских операций
 */
@Slf4j
@RestController
@Validated
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
@Tag(name = "ClientController", description = "Контроллер для взаимодействия с сервисом банковских операций")
public class ClientRestController {
    private final ClientService clientService;
    private final ClientAccountService clientAccountService;

    /**
     * метод, регистрирующий нового клиента в системе
     * @param client - объект Client
     * @return ClientReadDto - dto-объект, содержащий информацию о созданном клиенте
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientReadDto registerNewClient(@Valid @RequestBody ClientCreateEditDto client){
        ClientReadDto createdClient = clientService.createClient(client);

        ClientAccountCreateEditDto clientAccountCreateEditDto = ClientAccountCreateEditDto.builder()
                .clientId(createdClient.getId())
                .balance(client.getBalance())
                .build();
        ClientAccountReadDto clientAccountReadDto = clientAccountService.createClientAccount(clientAccountCreateEditDto);

        createdClient.setClientAccountReadDto(clientAccountReadDto);

        return createdClient;
    }

    /**
     * метод, добавляющий новый email клиенту
     * @param id - идентификатор клиента
     * @param email - email
     */
    @PatchMapping("/add-email/{id}")
    @SecurityRequirement(name = "JWT")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addEmailToClient(@PathVariable("id") Integer id, @Email @RequestParam String email){
        clientService.addEmailToClient(id, email);
    }

    /**
     * метод, добавляющий новый номер телефона клиенту
     * @param id - идентификатор клиента
     * @param phone - номер телефона
     */
    @PatchMapping("/add-phone/{id}")
    @SecurityRequirement(name = "JWT")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addPhoneToClient(@PathVariable("id") Integer id, @NotBlank @RequestParam String phone){
        clientService.addPhoneToClient(id, phone);
    }

    /**
     * метод, изменяющий существующий email клиента
     * @param id - идентификатор клиента
     * @param clientContactsDto - dto-объект необходимые для изменения контакта
     */
    @PatchMapping("/change-email/{id}")
    @SecurityRequirement(name = "JWT")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeClientEmail(@PathVariable("id") Integer id, @RequestBody ClientContactsDto clientContactsDto){
        clientService.changeClientEmail(id, clientContactsDto);
    }

    /**
     * метод, изменяющий существующий номер телефона клиента
     * @param id - идентификатор клиента
     * @param clientContactsDto - dto-объект необходимые для изменения контакта
     */
    @PatchMapping("/change-phone/{id}")
    @SecurityRequirement(name = "JWT")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeClientPhone(@PathVariable("id") Integer id, @RequestBody ClientContactsDto clientContactsDto){
        clientService.changeClientPhone(id, clientContactsDto);
    }

    /**
     * метод, удаляющий email клиента
     * @param id - идентификатор клиента
     * @param email - email
     */
    @DeleteMapping("/email/{id}")
    @SecurityRequirement(name = "JWT")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeClientEmail(@PathVariable("id") Integer id, @Email @RequestParam String email){
        clientService.removeClientEmail(id, email);
    }

    /**
     * метод, удаляющий номер телефона клиента
     * @param id - идентификатор клиента
     * @param phone - номер телефона
     */
    @DeleteMapping("/phone/{id}")
    @SecurityRequirement(name = "JWT")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeClientPhone(@PathVariable("id") Integer id, @NotBlank @RequestParam String phone){
        clientService.removeClientPhone(id, phone);
    }

    /**
     * метод, возвращающий клиентов, рожденных позже указанной даты рождения, согласно пагинации
     * @param page - номер страницы/количество пропущенных страниц
     * @param size - количество элементов на странице
     * @param birthDate - дата рождения
     * @return Page<ClientReadDto> - клиенты
     */
    @GetMapping("/birthDate")
    @SecurityRequirement(name = "JWT")
    @ResponseStatus(HttpStatus.OK)
    public Page<ClientReadDto> getClientsByBirthDate(@NotNull @RequestParam int page,
                                                     @NotNull @RequestParam int size,
                                                     @RequestParam LocalDate birthDate){
        return clientService.getClientsByBirthDate(page, size, birthDate);
    }

    /**
     * метод, возвращающий клиентов по фио согласно пагинации
     * @param page - номер страницы
     * @param size - количество элементов на странице
     * @param fio - фио
     * @return Page<ClientReadDto> - клиенты
     */
    @GetMapping("/fio")
    @SecurityRequirement(name = "JWT")
    @ResponseStatus(HttpStatus.OK)
    public Page<ClientReadDto> getClientsByFio(@NotNull @RequestParam int page,
                                               @NotNull @RequestParam int size,
                                               @NotBlank @RequestParam String fio){
        return clientService.getClientsByFio(page, size, fio);
    }

    /**
     * метод, возвращающий клиентов по указанному email
     * @param email - email
     * @return ClientReadDto - dto-объект, содержащий информацию о клиентах
     */
    @GetMapping("/email")
    @SecurityRequirement(name = "JWT")
    @ResponseStatus(HttpStatus.OK)
    public ClientReadDto getClientByEmail(@Email @RequestParam String email){
        return clientService.getClientByEmail(email);
    }

    /**
     * метод, возвращающий клиентов по указанному номеру телефона
     * @param phone - номер телефона
     * @return ClientReadDto - dto-объект, содержащий информацию о клиентах
     */
    @GetMapping("/phone")
    @SecurityRequirement(name = "JWT")
    @ResponseStatus(HttpStatus.OK)
    public ClientReadDto getClientByPhone(@NotBlank @RequestParam String phone){
        return clientService.getClientByPhone(phone);
    }

    /**
     * метод, осуществляющий трансфер денежных средств со счета аутентифицированного на счет другого клинта
     * @param principal - Principal объект из SecurityContext
     * @param transferMoneyDto - dto-объект, содержащий информацию необходимую для трансфера денежных средств
     */
    @PatchMapping("/transferring")
    @SecurityRequirement(name = "JWT")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void transferMoney(Principal principal, @Valid @RequestBody TransferMoneyDto transferMoneyDto){
        clientAccountService.transferMoney(principal.getName(), transferMoneyDto);
    }
}