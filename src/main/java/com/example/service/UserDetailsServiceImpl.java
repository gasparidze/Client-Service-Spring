package com.example.service;

import com.example.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Класс, реализующий интерфейс UserDetailsService
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final ClientRepository clientRepository;

    /**
     * метод, загружающий пользователя по логину
     * @param login - логие
     * @return UserDetails - одна из реализаций UserDetails
     * @throws UsernameNotFoundException - exception, если не удалось загрузить пользователя
     */
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return clientRepository.findClientByLogin(login)
                .map(client -> new User(
                        client.getLogin(),
                        client.getPassword(),
                        new ArrayList<>()
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user: " + login));
    }
}
