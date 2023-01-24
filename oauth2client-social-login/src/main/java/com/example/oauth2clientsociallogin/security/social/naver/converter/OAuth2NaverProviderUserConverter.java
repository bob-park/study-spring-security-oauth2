package com.example.oauth2clientsociallogin.security.social.naver.converter;

import org.springframework.security.oauth2.client.registration.ClientRegistration;

import com.example.oauth2clientsociallogin.security.converters.ProviderUserConverter;
import com.example.oauth2clientsociallogin.security.converters.ProviderUserRequest;
import com.example.oauth2clientsociallogin.security.model.ProviderUser;
import com.example.oauth2clientsociallogin.security.social.naver.model.NaverUser;
import com.example.oauth2clientsociallogin.security.type.SocialType;
import com.example.oauth2clientsociallogin.security.utils.OAuth2Utils;

public class OAuth2NaverProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {

    @Override
    public ProviderUser converter(ProviderUserRequest providerUserRequest) {

        ClientRegistration clientRegistration = providerUserRequest.clientRegistration();

        SocialType socialType = SocialType.findByName(clientRegistration.getRegistrationId());

        if (socialType != SocialType.NAMER) {
            return null;
        }

        return new NaverUser(
            OAuth2Utils.getSubAttributes(providerUserRequest.oAuth2User(), "response"),
            providerUserRequest.oAuth2User(),
            clientRegistration);
    }
}
