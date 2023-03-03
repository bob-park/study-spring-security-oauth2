package com.example.oauth2authorization03.provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final RegisteredClientRepository registeredClientRepository;
    private final OAuth2AuthorizationService oAuth2AuthorizationService;
    private final OAuth2AuthorizationConsentService oAuth2AuthorizationConsentService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication =
            (OAuth2AuthorizationCodeRequestAuthenticationToken) authentication;

        OAuth2AuthorizationCodeRequestAuthenticationProvider authenticationProvider
            = new OAuth2AuthorizationCodeRequestAuthenticationProvider(registeredClientRepository,
            oAuth2AuthorizationService, oAuth2AuthorizationConsentService);

        Authentication authenticate = authenticationProvider.authenticate(authorizationCodeRequestAuthentication);

        Authentication principal = (Authentication) authorizationCodeRequestAuthentication.getPrincipal();
        log.debug("principal={}", principal);

        return authenticate;

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2AuthorizationCodeRequestAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
