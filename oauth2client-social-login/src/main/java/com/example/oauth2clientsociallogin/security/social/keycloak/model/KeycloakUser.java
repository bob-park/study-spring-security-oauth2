package com.example.oauth2clientsociallogin.security.social.keycloak.model;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.example.oauth2clientsociallogin.security.model.OAuth2ProviderUser;

public class KeycloakUser extends OAuth2ProviderUser {

    public KeycloakUser(OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        super(oAuth2User.getAttributes(), oAuth2User, clientRegistration);

    }

    @Override
    public String getId() {
        return (String) getAttributes().get("sub");
    }

    @Override
    public String getUsername() {
        return (String) getAttributes().get("preferred_username");
    }

    @Override
    public String getPicture() {
        return null;
    }
}
