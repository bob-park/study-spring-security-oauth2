package com.bob.oauth2client.controller;

import java.time.Instant;
import java.util.Map;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class IndexController {

    private final ClientRegistrationRepository clientRegistrationRepository;

    @GetMapping(path = "")
    public String index() {
        return "index";
    }

    /*
     * Authorization Server 에서 해주지만, 예제로 어떻게 이루어지는지 확인용
     */
    @GetMapping(path = "user")
    public OAuth2User user(String accessToken) {

        ClientRegistration clientRegistration =
            clientRegistrationRepository.findByRegistrationId("keycloak");

        OAuth2AccessToken oAuth2AccessToken =
            new OAuth2AccessToken(TokenType.BEARER, accessToken, Instant.now(), Instant.MAX);

        OAuth2UserRequest oAuth2UserRequest =
            new OAuth2UserRequest(clientRegistration, oAuth2AccessToken);

        DefaultOAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();

        return oAuth2UserService.loadUser(oAuth2UserRequest);

    }

    @GetMapping(path = "oidc")
    public OAuth2User oidc(String accessToken, String idToken) {

        ClientRegistration clientRegistration =
            clientRegistrationRepository.findByRegistrationId("keycloak");

        OAuth2AccessToken oAuth2AccessToken =
            new OAuth2AccessToken(TokenType.BEARER, accessToken, Instant.now(), Instant.MAX);

        Map<String, Object> idTokenClaims = Maps.newHashMap();

        idTokenClaims.put(IdTokenClaimNames.ISS, "http://localhost:8080/realms/oauth2");
        idTokenClaims.put(IdTokenClaimNames.SUB, "OIDC");
        idTokenClaims.put("preferred_username", "user");

        OidcIdToken oidcIdToken =
            new OidcIdToken(idToken, Instant.now(), Instant.MAX, idTokenClaims);

        OidcUserRequest oidcUserRequest =
            new OidcUserRequest(clientRegistration, oAuth2AccessToken, oidcIdToken);

        OidcUserService oAuth2UserService = new OidcUserService();

        return oAuth2UserService.loadUser(oidcUserRequest);

    }

}
