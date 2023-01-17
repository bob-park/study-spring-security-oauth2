package com.example.oauth2clientsociallogin.naver.model;

import java.util.Map;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.example.oauth2clientsociallogin.security.model.OAuth2ProviderUser;

public class NaverUser extends OAuth2ProviderUser {

    @SuppressWarnings("unchecked")
    public NaverUser(OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        super((Map<String, Object>) oAuth2User.getAttributes().get("response"), oAuth2User, clientRegistration);

    }

    @Override
    public String getId() {
        return (String) getAttributes().get("id");
    }

    @Override
    public String getUsername() {
        return (String) getAttributes().get("email");
    }

}
