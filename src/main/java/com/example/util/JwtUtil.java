package com.example.util;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import java.util.function.Function;

/**
 * Util-класс для работы с jwt токеном
 */
@Slf4j
public class JwtUtil {

    /**
     * generic-метод, извлекающий нужную информацию из токена в request в зависимости от переданного лямбда-выражения
     * @param request - http запрос
     * @param function - лямбда-вырежение
     * @return T - информация из токена
     * @param <T> - generic
     */
    public static <T> T getClaim(HttpServletRequest request, Function<String, T> function){
        String authHeader = request.getHeader("Authorization");
        String jwt;
        T claim = null;

        if(authHeader != null && authHeader.startsWith("Bearer ")){
            jwt = authHeader.substring(7);
            try {
                claim = function.apply(jwt);
            } catch (ExpiredJwtException e) {
                log.debug("Время жизни токена вышла");
            }
        }

        return claim;
    }
}
