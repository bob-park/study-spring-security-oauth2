package com.example.oauth2clientsociallogin.security.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.example.oauth2clientsociallogin.google.model.GoogleUser;
import com.example.oauth2clientsociallogin.keycloak.model.KeycloakUser;
import com.example.oauth2clientsociallogin.naver.model.NaverUser;
import com.example.oauth2clientsociallogin.security.model.ProviderUser;
import com.example.oauth2clientsociallogin.security.model.User;
import com.example.oauth2clientsociallogin.security.repository.UserRepository;

@Slf4j
@RequiredArgsConstructor
@Getter
public abstract class AbstractOAuth2UserService {

    public static final String KEYCLOAK = "keycloak";
    public static final String GOOGLE = "google";
    public static final String NAVER = "naver";


    private UserRepository userRepository;
    private UserService userService;

    protected ProviderUser providerUser(ClientRegistration clientRegistration, OAuth2User oAuth2User) {

        String registrationId = clientRegistration.getRegistrationId();

        switch (registrationId) {

            case KEYCLOAK:
                return new KeycloakUser(oAuth2User, clientRegistration);

            case GOOGLE:
                return new GoogleUser(oAuth2User, clientRegistration);

            case NAVER:
                return new NaverUser(oAuth2User, clientRegistration);

            default:
                return null;
        }

    }

    protected void register(ProviderUser providerUser, OAuth2UserRequest userRequest) {

        User user = userRepository.findByUsername(providerUser.getUsername());

        if (user == null) {
            userService.register(userRequest.getClientRegistration().getRegistrationId(), providerUser);
        } else {
            log.debug("user={}", user);
        }

    }


    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
