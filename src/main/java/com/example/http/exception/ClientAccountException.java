package com.example.http.exception;

/**
 * Класс, отвечающий за ошибки при взаимодействии со счетом клиента (объект ClientAccount)
 */
public class ClientAccountException extends RuntimeException{
    public ClientAccountException(String message) {
        super(message);
    }
}
