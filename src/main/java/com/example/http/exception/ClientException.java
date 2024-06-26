package com.example.http.exception;

/**
 * Класс, отвечающий за ошибки при взаимодействии с клиентом (объект Client)
 */
public class ClientException extends RuntimeException {
    public ClientException(String message) {
        super(message);
    }
}
