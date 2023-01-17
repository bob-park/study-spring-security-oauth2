package com.example.oauth2clientsociallogin.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.example.oauth2clientsociallogin.security.model.ProviderUser;
import com.example.oauth2clientsociallogin.security.model.User;
import com.example.oauth2clientsociallogin.security.repository.UserRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public void register(String registrationId, ProviderUser user) {

        User createdUser =
            User.builder()
                .registrationId(registrationId)
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .authorities(user.getAuthorities())
                .build();

        userRepository.register(createdUser);
    }

}
