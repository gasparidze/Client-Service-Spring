package com.example.service;

import com.example.entity.Client;
import com.example.dto.JwtRequestDto;
import com.example.dto.JwtResponseDto;
import com.example.http.exception.AuthException;
import com.example.provider.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * Сервис, отвечающий за аутентификацию клиентов в системе
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    private final ClientService clientService;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authManager;

    /**
     * метод, аутентифицирующий клиента
     * @param jwtRequestDto - dto-объект, содержащий необходимую информацию для аутентификации
     * @return JwtResponseDto
     */
    public JwtResponseDto createAuthToken(JwtRequestDto jwtRequestDto) {
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(
                    jwtRequestDto.getLogin(),
                    jwtRequestDto.getPassword())
            );
        } catch (BadCredentialsException e){
            throw new AuthException("Неверный логин или пароль");
        }

        Client client = clientService.getClientByLogin(jwtRequestDto.getLogin());
        String jwt = jwtProvider.generateToken(client);
        return new JwtResponseDto(jwt);
    }
}
