package com.example.oauth2clientsociallogin.security.social.google.model;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.example.oauth2clientsociallogin.security.model.Attributes;
import com.example.oauth2clientsociallogin.security.model.OAuth2ProviderUser;

public class GoogleUser extends OAuth2ProviderUser {


    public GoogleUser(Attributes attributes, OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        super(attributes.getMainAttributes(), oAuth2User, clientRegistration);

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
