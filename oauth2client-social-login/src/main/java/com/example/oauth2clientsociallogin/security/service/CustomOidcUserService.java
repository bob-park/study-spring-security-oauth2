package com.example.oauth2clientsociallogin.security.service;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import com.example.oauth2clientsociallogin.security.converters.ProviderUserRequest;
import com.example.oauth2clientsociallogin.security.model.PrincipalUser;
import com.example.oauth2clientsociallogin.security.model.ProviderUser;

@Service
public class CustomOidcUserService extends AbstractOAuth2UserService implements
    OAuth2UserService<OidcUserRequest, OidcUser> {

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {

        ClientRegistration clientRegistration = userRequest.getClientRegistration();

        // ! oidc 의 user-name-attribute 가 기본적으로 sub 이다.
        clientRegistration = ClientRegistration.withClientRegistration(clientRegistration)
            .userNameAttributeName("sub")
            .build();

        userRequest =
            new OidcUserRequest(clientRegistration,
                userRequest.getAccessToken(),
                userRequest.getIdToken(),
                userRequest.getAdditionalParameters());

        OAuth2UserService<OidcUserRequest, OidcUser> oidc2UserService = new OidcUserService();

        OidcUser oidcUser = oidc2UserService.loadUser(userRequest);

        ProviderUserRequest providerUserRequest = new ProviderUserRequest(clientRegistration, oidcUser);

        ProviderUser providerUser = super.providerUser(providerUserRequest);

        // 회원가입
        super.register(providerUser, userRequest);

        return new PrincipalUser(providerUser);
    }
}
