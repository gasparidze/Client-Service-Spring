package com.example.http.controller;

import com.example.dto.*;
import com.example.service.ClientAccountService;
import com.example.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
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
    @Operation(
            summary = "Регистрация клиента",
            description = "Позволяет создать нового клиента в системе"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientReadDto registerNewClient(@Valid @RequestBody ClientCreateEditDto client){
        return clientService.createClient(client);
    }

    /**
     * метод, добавляющий новый email клиенту
     * @param id - идентификатор клиента
     * @param email - email
     */
    @Operation(
            summary = "Добавление email клиенту",
            description = "Позволяет добавлять email клиенту по его id"
    )
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
    @Operation(
            summary = "Добавление номера телефона клиенту",
            description = "Позволяет добавлять номера телефона клиенту по его id"
    )
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
    @Operation(
            summary = "Изменение email клиента",
            description = "Позволяет изменять существующий email клиента по его id"
    )
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
    @Operation(
            summary = "Изменение номера телефона клиента",
            description = "Позволяет изменять существующий номера телефона клиента по его id"
    )
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
    @Operation(
            summary = "Удаление email",
            description = "Позволяет удалять email клиента по его id"
    )
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
    @Operation(
            summary = "Удаление номера телефона",
            description = "Позволяет удалять номера телефона клиента по его id"
    )
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
    @Operation(
            summary = "Поиск клиентов по дате рождения",
            description = "Позволяет найти всех клиентов в системе рожденных позже указанной даты рождения"
    )
    @GetMapping("/birthDate")
    @SecurityRequirement(name = "JWT")
    @ResponseStatus(HttpStatus.OK)
    public Slice<ClientReadDto> getClientsByBirthDate(@NotNull @RequestParam(value = "offset", defaultValue = "0") int page,
                                                     @NotNull @RequestParam(value = "limit") int size,
                                                     @RequestParam LocalDate birthDate){
        return clientService.getClientsByBirthDate(page, size, birthDate);
    }

    /**
     * метод, возвращающий клиентов по указанному email
     * @param email - email
     * @return ClientReadDto - dto-объект, содержащий информацию о клиентах
     */
    @Operation(
            summary = "Поиск клиентов по email",
            description = "Позволяет найти всех клиентов в системе по email"
    )
    @GetMapping("/email")
    @SecurityRequirement(name = "JWT")
    @ResponseStatus(HttpStatus.OK)
    public Slice<ClientReadDto> getClientsByEmail(@NotNull @RequestParam(value = "offset", defaultValue = "0") int page,
                                                  @NotNull @RequestParam(value = "limit") int size,
                                                  @Email @RequestParam String email){
        return clientService.getClientsByEmail(page, size, email);
    }

    /**
     * метод, возвращающий клиентов по фио согласно пагинации
     * @param page - номер страницы
     * @param size - количество элементов на странице
     * @param fio - фио
     * @return Page<ClientReadDto> - клиенты
     */
    @Operation(
            summary = "Поиск клиентов по фио",
            description = "Позволяет найти всех клиентов в системе по фио"
    )
    @GetMapping("/fio")
    @SecurityRequirement(name = "JWT")
    @ResponseStatus(HttpStatus.OK)
    public Slice<ClientReadDto> getClientsByFio(@NotNull @RequestParam(value = "offset", defaultValue = "0") int page,
                                                @NotNull @RequestParam(value = "limit") int size,
                                                @NotBlank @RequestParam String fio){
        return clientService.getClientsByFio(page, size, fio);
    }

    /**
     * метод, возвращающий клиентов по указанному номеру телефона
     * @param phone - номер телефона
     * @return ClientReadDto - dto-объект, содержащий информацию о клиентах
     */
    @Operation(
            summary = "Поиск клиентов по номеру телефона",
            description = "Позволяет найти всех клиентов в системе по номеру телефона"
    )
    @GetMapping("/phone")
    @SecurityRequirement(name = "JWT")
    @ResponseStatus(HttpStatus.OK)
    public Slice<ClientReadDto> getClientByPhone(@NotNull @RequestParam(value = "offset", defaultValue = "0") int page,
                                          @NotNull @RequestParam(value = "limit") int size,
                                          @NotBlank @RequestParam String phone){
        return clientService.getClientsByPhone(page, size, phone);
    }

    /**
     * метод, осуществляющий трансфер денежных средств со счета аутентифицированного на счет другого клинта
     * @param principal - Principal объект из SecurityContext
     * @param transferMoneyDto - dto-объект, содержащий информацию необходимую для трансфера денежных средств
     */
    @Operation(
            summary = "Перевод денежных средств",
            description = "Позволяет перевести денежные средства с одного счета клиента другому"
    )
    @PatchMapping("/transferring")
    @SecurityRequirement(name = "JWT")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void transferMoney(Principal principal, @Valid @RequestBody TransferMoneyDto transferMoneyDto){
        clientAccountService.transferMoney(principal.getName(), transferMoneyDto);
    }
}