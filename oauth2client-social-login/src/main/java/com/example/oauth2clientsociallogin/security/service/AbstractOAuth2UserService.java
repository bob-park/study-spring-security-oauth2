package com.example.oauth2clientsociallogin.security.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

import com.example.oauth2clientsociallogin.security.converters.ProviderUserConverter;
import com.example.oauth2clientsociallogin.security.converters.ProviderUserRequest;
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
    private ProviderUserConverter<ProviderUserRequest, ProviderUser> providerUserConverter;


    protected ProviderUser providerUser(ProviderUserRequest providerUserRequest) {

        return providerUserConverter.converter(providerUserRequest);

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

    @Autowired
    public void setProviderUserConverter(
        ProviderUserConverter<ProviderUserRequest, ProviderUser> providerUserConverter) {
        this.providerUserConverter = providerUserConverter;
    }
}
