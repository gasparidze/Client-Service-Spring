package com.example.provider;

import com.example.entity.Client;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.function.Function;

/**
 * Класс, отвечающий за работу с токенами
 */
@Component
public class JwtProvider {
    /**
     * secret_key для подписания и сверки токена
     */
    private final SecretKey jwtSecretKey;

    /**
     * время жизни токена
     */
    private final Duration lifetime;

    public JwtProvider(@Value("${jwt.secret}") String jwtSecretKey,
                       @Value("${jwt.lifetime}") Duration lifetime) {
        this.jwtSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretKey));
        this.lifetime = lifetime;
    }

    /**
     * метод, генерирующий jwt токен
     * @param client - объект Client
     * @return String - jwt токен
     */
    public String generateToken(Client client){
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + lifetime.toMillis());

        return Jwts.builder()
                .claim("clientId", client.getId())
                .subject(client.getLogin())
                .issuedAt(issuedDate)
                .expiration(expiredDate)
                .signWith(jwtSecretKey)
                .compact();
    }

    /**
     * метод, извлекающий логин из токена
     * @param jwt - jwt токен
     * @return String - логин
     */
    public String getLogin(String jwt){
        return getClaim(jwt, Claims::getSubject);
    }

    /**
     * generic-метод, извлекающий заданную в claimsResolver информацию из токена
     * @param jwt - jwt токен
     * @param claimsResolver - лямбда-выражение
     * @return T - информация, извлеченная в соответствии с лямбда-выражением
     * @param <T> - generic
     */
    public <T> T getClaim(String jwt, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(jwt);
        return claimsResolver.apply(claims);
    }

    /**
     * метод, извлекающий все claims из токена
     * @param jwt - jwt токен
     * @return Claims - claims из токена
     */
    private Claims getAllClaims(String jwt){
        return Jwts.parser()
                .verifyWith(jwtSecretKey)
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }
}
