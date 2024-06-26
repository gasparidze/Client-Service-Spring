package com.example.util;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

/**
 * Класс для генерации secret_key, хранящийся в application.yaml
 */
public class GenerateKeys {
    public static void main(String[] args) {
        System.out.println(generateKey());
    }

    /**
     * метод, генерирующий secret_key
     * @return String - secret_key
     */
    private static String generateKey() {
        return Encoders.BASE64.encode(Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded());
    }
}
