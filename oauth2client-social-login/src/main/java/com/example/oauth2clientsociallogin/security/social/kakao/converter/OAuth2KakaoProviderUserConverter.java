package com.example.oauth2clientsociallogin.security.social.kakao.converter;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import com.example.oauth2clientsociallogin.security.converters.ProviderUserConverter;
import com.example.oauth2clientsociallogin.security.converters.ProviderUserRequest;
import com.example.oauth2clientsociallogin.security.model.ProviderUser;
import com.example.oauth2clientsociallogin.security.social.google.model.GoogleUser;
import com.example.oauth2clientsociallogin.security.social.kakao.model.KakaoUser;
import com.example.oauth2clientsociallogin.security.type.SocialType;
import com.example.oauth2clientsociallogin.security.utils.OAuth2Utils;

public class OAuth2KakaoProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {

    @Override
    public ProviderUser converter(ProviderUserRequest providerUserRequest) {

        ClientRegistration clientRegistration = providerUserRequest.clientRegistration();

        SocialType socialType = SocialType.findByName(clientRegistration.getRegistrationId());

        if (socialType != SocialType.KAKAO) {
            return null;
        }

        if(providerUserRequest.oAuth2User() instanceof OidcUser){
            return null;
        }

        return new KakaoUser(
            OAuth2Utils.getOtherAttributes(providerUserRequest.oAuth2User(), "kakao_account", "profile"),
            providerUserRequest.oAuth2User(),
            clientRegistration);
    }
}
