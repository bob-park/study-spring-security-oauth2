package com.example.oauth2clientsociallogin.google.model;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.example.oauth2clientsociallogin.security.model.OAuth2ProviderUser;

public class GoogleUser extends OAuth2ProviderUser {


    public GoogleUser(OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        super(oAuth2User.getAttributes(), oAuth2User, clientRegistration);

    }

    @Override
    public String getId() {
        return (String) getAttributes().get("sub"); // google 은 sub 에 id + username 으로 되어있음
    }

    @Override
    public String getUsername() {
        return (String) getAttributes().get("sub");
    }


}
