package com.example.oauth2clientsociallogin.security.social.google.converter;

import org.springframework.security.oauth2.client.registration.ClientRegistration;

import com.example.oauth2clientsociallogin.security.converters.ProviderUserConverter;
import com.example.oauth2clientsociallogin.security.converters.ProviderUserRequest;
import com.example.oauth2clientsociallogin.security.model.ProviderUser;
import com.example.oauth2clientsociallogin.security.social.google.model.GoogleUser;
import com.example.oauth2clientsociallogin.security.type.SocialType;
import com.example.oauth2clientsociallogin.security.utils.OAuth2Utils;

public class OAuth2GoogleProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {

    @Override
    public ProviderUser converter(ProviderUserRequest providerUserRequest) {

        ClientRegistration clientRegistration = providerUserRequest.clientRegistration();

        SocialType socialType = SocialType.findByName(clientRegistration.getRegistrationId());

        if (socialType != SocialType.GOOGLE) {
            return null;
        }

        return new GoogleUser(
            OAuth2Utils.getMainAttributes(providerUserRequest.oAuth2User()),
            providerUserRequest.oAuth2User(),
            clientRegistration);
    }
}
